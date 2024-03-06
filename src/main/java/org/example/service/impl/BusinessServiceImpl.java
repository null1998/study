package org.example.service.impl;

import com.alibaba.ttl.TtlRunnable;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.example.annotation.GetUsingTable;
import org.example.dao.RegionMapper;
import org.example.entity.Region;
import org.example.service.IBusinessService;
import org.example.util.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author huang
 */
@Service
public class BusinessServiceImpl implements IBusinessService {
    private RegionMapper regionMapper;

    private Executor executor;

    @Autowired
    public BusinessServiceImpl(RegionMapper regionMapper,
                               @Qualifier("businessThreadPool") Executor executor) {
        this.regionMapper = regionMapper;
        this.executor = executor;
    }

    @GetUsingTable(busType = {"type_A", "type_B"})
    @Override
    public String business(String param) {
//        regionMapper.selectByPrimaryKey(1);
        regionMapper.selectByIds(Lists.newArrayList(1, 2, 3));
//        regionMapper.selectAll();
//        regionMapper.insert(new Region());
//        regionMapper.deleteByPrimaryKey(1);
//        regionMapper.deleteByIds(Lists.newArrayList(1,2,3));
//        regionMapper.updateByPrimaryKey(new Region());
        executor.execute(TtlRunnable.get(() -> regionMapper.selectByPrimaryKey(1)));
        return param + LocalDateTime.now();
    }

    @GetUsingTable(busType = {"type_A", "type_B", "type_C"})
    @Override
    public void businessInsert(Integer id) {
        Region region = new Region();
        region.setId(id);
        region.setName("id_" + id);
        region.setNum(1);
        int insert = regionMapper.insert(region);
        if (insert != 1) {
            throw new RuntimeException("插入错误");
        }
    }

    @GetUsingTable(busType = {"type_A"})
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void businessQueryAndDelete(Integer id) {
        Region region = regionMapper.selectByPrimaryKey(id);
        if (region != null) {
            int delete = regionMapper.deleteByPrimaryKey(id);
            if (delete != 1) {
                throw new RuntimeException("删除错误");
            }
        }
    }

    @GetUsingTable(busType = {"type_A"})
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void businessQueryAllAndDelete() {
        List<Region> regionList = regionMapper.selectAll();
        for (Region region : regionList) {
            int delete = regionMapper.deleteByPrimaryKey(region.getId());
            if (delete != 1) {
                throw new RuntimeException("删除错误");
            }
        }
    }

    @GetUsingTable(busType = {"type_A"})
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void businessAsync(Integer id) {
        Region region = new Region();
        region.setId(id);
        region.setName("id_" + id);
        region.setNum(1);
        int insert = regionMapper.insert(region);
        if (insert != 1) {
            throw new RuntimeException("插入错误");
        }
        executor.execute(TtlRunnable.get(() -> {
            ThreadUtil.sleep(300);
            int delete = regionMapper.deleteByPrimaryKey(region.getId());
            if (delete != 1) {
                throw new RuntimeException("删除错误");
            }
        }));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void businessMultiInsert(Integer startId, Integer number) {
        List<Region> regionList = new ArrayList<>();
        for (int i = startId; i < startId + number; i++) {
            Region region = new Region();
            region.setId(i);
            region.setName("id_" + i);
            region.setNum(1);
            regionList.add(region);
        }
        for (List<Region> partition : ListUtils.partition(regionList, 10000)) {
            regionMapper.insertBatch(partition);
        }
    }

    @Override
    public void businessDelete(Integer id) {
        regionMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void businessMultiDelete(Integer startId, Integer number) {
        for (int i = startId; i < startId + number; i++) {
            if (i % 2 == 0) {
                continue;
            }
            regionMapper.deleteByPrimaryKey(i);
        }
    }

    @Override
    public void businessIncreaseNum(Integer id) {
        int update = regionMapper.increaseNum(id);
        if (update != 1) {
            throw new RuntimeException("更新错误");
        }
    }
}
