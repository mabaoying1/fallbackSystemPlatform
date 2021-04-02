package com.soft.util;
import com.alibaba.fastjson.JSON;
import com.soft.Entity.ResponseEntity;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import com.soft.Entity.SysParameterEntity;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ElasticsearchUtil
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/26
 * @Version V1.0
 **/
@Component
public class ElasticsearchUtil {
    private final Logger logger = LoggerFactory.getLogger(ElasticsearchUtil.class);
    @Autowired
    private SysParameterEntity sysParameterEntity;

    public TransportClient connectElasticsearch() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，docker-cluster是ES集群的名称
        Settings settings = Settings.builder()
                //设置es集群名称
                .put("cluster.name", sysParameterEntity.getClusterName())
                //增加嗅探机制，找到es集群
                /*
                 *传输客户端带有集群嗅探功能，允许动态添加新主机和删除旧主机。 <br>
                 * 当启用嗅探时，传输客户机将连接到其内部节点列表中的节点，该节点列表是通过调用addTransportAddress构建的。<br>
                 * 之后，客户端将调用这些节点上的内部集群状态API来发现可用的数据节点。<br>
                 * 客户端的内部节点列表将仅替换为这些数据节点。<br>
                 * 默认情况下，该列表每5秒刷新一次。<br>
                 * 注意，嗅探器连接到的IP地址是那些节点的Elasticsearch配置中声明为发布地址的IP地址<br>
                 */
                .put("client.transport.sniff", true)
                .build();
        /*
         * 创建客户端
         */
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddresses(
                new InetSocketTransportAddress(InetAddress.getByName(sysParameterEntity.getElasticsearchHost()),
                        Integer.parseInt(sysParameterEntity.getElasticsearchPort())));
        return client;
    }

    public void closeElasticsearch(TransportClient client){
        try {
            if(client != null){
                client.close();
            }
        }catch (Exception e){
            logger.error("Elasticsearch 关闭连接异常【"+e.getMessage()+"】");
        }
    }

    /**
     * 新增数据 Elasticsearch<br>
     * @param mapData 保存的数据，由于版本的原因，此处不支持JSON <br>
     * @param index 索引,索引命名规范这个名称必须全部小写，不能以下划线开头，不能包含逗号。<br>
     * @param type 类型,type名称可以是大写或者小写，但是同时不能用下划线开头，不能包含逗号<br>
     * @param id 代表document 的唯一标识，与index和type一起，可以唯一标识和定位一个保存的数据<br>
     * @return
     */
    public ResponseEntity addElasticsearchData(Map mapData, String index
            , String type, String id){
        TransportClient client = null;
        try {
            client = connectElasticsearch();
            //index :索引名 type:类型  :id
            IndexResponse response = client.prepareIndex(index, type, id)
                    //.setSource(mapEntry,XContentType.JSON) 可以指定JSON类型
                    .setSource(mapData)
                    //.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE) 代表插入成功后立即刷新,
                    // 因为ES中插入数据默认分片要1秒钟后再刷新
                    .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).get();
            if(response.getResult().getLowercase().equals("created")){
                return ResultMessage.success("操作成功");
            }
            return ResultMessage.error("操作失败");
        }catch (UnknownHostException e){
            e.printStackTrace();
            logger.error("Elasticsearch 初始化失败");
            return ResultMessage.error(500,"UnknownHostException: "+e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Elasticsearch【addElasticsearchData】数据失败;数据【"+mapData+"】");
            return ResultMessage.error(500,e.getMessage());
        }finally {
            closeElasticsearch(client);
        }
    }

    /**
     * 根据ID, 查询 Elasticsearch 数据<br>
     * @param index 索引,<br>
     * @param type 类型,<br>
     * @param id <br>
     * @return
     */
    public ResponseEntity queryElasticsearchDataByID(String index, String type, String id){
        TransportClient client = null;
        try {
            client = connectElasticsearch();
            //实现数据查询(指定_id查询) 参数分别是 索引名，类型名  id
            GetResponse getResponse = client.prepareGet(index, type, id).get();
            String sourceAsString = getResponse.getSourceAsString();
            return ResultMessage.success(200,"查询成功",sourceAsString);
        }catch (UnknownHostException e){
            e.printStackTrace();
            logger.error("Elasticsearch 初始化失败");
            return ResultMessage.error(500,"UnknownHostException: "+e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Elasticsearch【queryElasticsearchDataByID】数据失败;数据【"+id+"】");
            return ResultMessage.error(500,e.getMessage());
        }finally {
            closeElasticsearch(client);
        }
    }

    /**
     * 查询 Elasticsearch 所有数据<br>
     * @param index 索引,<br>
     * @return
     */
    public ResponseEntity queryElasticsearchDataAll(String index){
        TransportClient client = null;
        List<Map> rel = new ArrayList<Map>();
        try {
            client = connectElasticsearch();
            QueryBuilder qBuilder = QueryBuilders.matchAllQuery();
            SearchResponse sResponse = client.prepareSearch(index)
                    .setQuery(qBuilder)
                    .get();
            SearchHits hits = sResponse.getHits();
            for (SearchHit hit : hits) {
                //将获取的值转换成map的形式
                Map<String, Object> map = hit.getSourceAsMap();
                rel.add(map);
            }
            return ResultMessage.success(200,"查询成功",rel);
        }catch (UnknownHostException e){
            e.printStackTrace();
            logger.error("Elasticsearch 初始化失败");
            return ResultMessage.error(500,"UnknownHostException: "+e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Elasticsearch【queryElasticsearchDataByID】数据失败;数据【"+index+"】");
            return ResultMessage.error(500,e.getMessage());
        }finally {
            closeElasticsearch(client);
        }
    }



    /**
     * 根据id删除指定索引、类型下的文档，id需先通过搜索获取<br>
     * @param index 索引<br>
     * @param type  类型<br>
     * @param id    文档id<br>
     * @return
     */
    public ResponseEntity deleteElasticsearchIndex(String index,String type,String id) {
        TransportClient client = null;
        try {
            client = connectElasticsearch();
            DeleteResponse deleteResponse= client.prepareDelete(index, type, id).get();
            if("OK".equals(deleteResponse.status().toString())){
                return ResultMessage.success("删除成功");
            }
            return ResultMessage.error("删除失败");
        }catch (UnknownHostException e){
            e.printStackTrace();
            logger.error("Elasticsearch 初始化失败");
            return ResultMessage.error(500,"UnknownHostException: "+e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Elasticsearch【queryElasticsearchDataByID】数据失败;数据【"+id+"】");
            return ResultMessage.error(500,e.getMessage());
        }finally {
            closeElasticsearch(client);
        }
    }


    /**
     * 删除指定索引，慎用<br>
     * @param index 索引<br>
     * @return
     */
    public ResponseEntity deleteElasticsearchAll(String index){
        TransportClient client = null;
        try {
            client = connectElasticsearch();
            DeleteIndexResponse deleteIndexResponse= client.admin().indices().prepareDelete(index).get();
            return ResultMessage.success(200,"删除成功",  deleteIndexResponse);
        }catch (UnknownHostException e){
            e.printStackTrace();
            logger.error("Elasticsearch 初始化失败");
            return ResultMessage.error(500,"UnknownHostException: "+e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Elasticsearch【deleteAll】数据失败;数据【"+index+"】");
            return ResultMessage.error(500,e.getMessage());
        }finally {
            closeElasticsearch(client);
        }
    }

    /**
     * 更新 Elasticsearch 数据<br>
     * @param newMapData 需要更新的数据<br>
     * @param index 索引<br>
     * @param type 类型<br>
     * @param id <br>
     * @return
     */
    public ResponseEntity modifyElasticsearchData(Map newMapData, String index
            , String type, String id){
        TransportClient client = null;
        try {
            client = connectElasticsearch();
            UpdateResponse updateResponse  = client.prepareUpdate(index,type,id).setDoc(newMapData).get();
            //控制台出现OK 代表更新成功
            if("OK".equals(updateResponse.status().toString())){
                return ResultMessage.success("更新成功");
            }
            return ResultMessage.error("更新失败");
        }catch (UnknownHostException e){
            e.printStackTrace();
            logger.error("Elasticsearch 初始化失败");
            return ResultMessage.error(500,"UnknownHostException: "+e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Elasticsearch【modifyElasticsearchData】数据失败;数据【"+newMapData+"】");
            return ResultMessage.error(500,e.getMessage());
        }finally {
            closeElasticsearch(client);
        }
    }

}
