package com.crud.demo.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SchedulingConfig.class)
public class SchedulingConfigTest {

    @Autowired
    private TaskScheduler taskScheduler;

    @Test
    void taskSchedulerBeanShouldBePresentAndCorrectType() {
        assertThat(taskScheduler).isNotNull();
        assertThat(taskScheduler).isInstanceOf(ConcurrentTaskScheduler.class);
    }
}
