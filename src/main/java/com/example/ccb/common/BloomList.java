package com.example.ccb.common;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 由布隆过滤器组成的列表
 */
@Component
public class BloomList {

    private List<BloomFilter<CharSequence>> list;

    //区块数
    private int blockCount = 0;

    public BloomList() {
        this.list = new ArrayList<BloomFilter<CharSequence>>(10000);
        for (int i = 0; i < 10000; i++) {
            list.add(BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), 5000, 0.000001));
        }
    }

    /**
     * 寻找含有target关键字的目标区块号
     * @param target
     * @return  区块号
     */
    public int search(String target) {
        int t = getT(blockCount);
        int imax = Math.max((1 << (t - 1)) - 1, blockCount - (1 << t));

        for (int i = 0; i <= imax; i++) {//每次搜索都从第零号开始
            if (hit(target, i)) {//第一次命中了
                int h = getT(i) + 1;//优化算法
                while (i + (1 << h) < blockCount) {
                    if (hit(target, i + (1 << h))) {//如果第二次命中
                        return i + (1 << (h + 1));//根据搜索算法可得到目标
                    } else {
                        h++;
                    }
                }
            }
        }
        return -1;//代表没结果
    }

    /**
     * 判断n号布隆过滤器是否可以找到target
     * @param target
     * @param n
     * @return
     */
    public boolean hit(String target, int n) {
        return list.get(n).mightContain(target);
    }


    public int getT(int n) {
        int i = 0;
        while (i < n) {
            if ((1 << i) > n) {
                break;
            }
            i++;
        }
        return i - 1;
    }

    /**
     * 向数据结构中增加Key 需要将其增添到对应的布隆过滤器中
     * @param key
     * @param blockId
     */
    public void addKey(String key, int blockId) {
        blockCount++;
        int i = 0;
        while (blockId - (1 << i) >= 0) {
            int filterId = blockId - (1 << i);
            //向区块对应的布隆过滤器添加Key
            list.get(filterId).put(key);
            i++;
        }
    }

}
