package com.yupi.codertestbackend.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.yupi.codertestbackend.mapper.LevelMapper;
import com.yupi.codertestbackend.model.entity.Level;
import com.yupi.codertestbackend.service.LevelService;
import org.springframework.stereotype.Service;

/**
 * 关卡 Service 实现
 */
@Service
public class LevelServiceImpl extends ServiceImpl<LevelMapper, Level> implements LevelService {
}
