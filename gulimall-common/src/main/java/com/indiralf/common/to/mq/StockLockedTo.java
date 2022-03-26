package com.indiralf.common.to.mq;

import com.indiralf.common.to.StockDetailTo;
import lombok.Data;


/**
 * @author
 * @time 2022/3/18 15:40
 * @Description- TODO
 */
@Data
public class StockLockedTo {
    /**
     * 库存工作单id
     */
    private Long id;
    /**
     * 工作单详情的id
     */
    private StockDetailTo detail;
}
