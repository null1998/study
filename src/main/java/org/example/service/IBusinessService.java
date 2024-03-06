package org.example.service;

/**
 * @author huang
 */
public interface IBusinessService {
    /**
     * 业务方法
     *
     * @param param 参数
     * @return {@link String}
     */
    String business(String param);

    /**
     * 插入
     *
     * @param id 主键
     */
    void businessInsert(Integer id);

    /**
     * 查询并删除
     *
     * @param id 主键
     */
    void businessQueryAndDelete(Integer id);

    /**
     * 查询全部并删除
     */
    void businessQueryAllAndDelete();

    /**
     * 异步方法
     *
     * @param id 主键
     */
    void businessAsync(Integer id);

    /**
     * 批量插入
     *
     * @param startId 起始主键
     * @param number  数量
     */
    void businessMultiInsert(Integer startId, Integer number);

    /**
     * 删除
     *
     * @param id 主键
     */
    void businessDelete(Integer id);

    /**
     * 批量删除
     *
     * @param startId 起始主键
     * @param number  数量
     */
    void businessMultiDelete(Integer startId, Integer number);

    /**
     * 增长num值
     *
     * @param id 主键
     */
    void businessIncreaseNum(Integer id);
}
