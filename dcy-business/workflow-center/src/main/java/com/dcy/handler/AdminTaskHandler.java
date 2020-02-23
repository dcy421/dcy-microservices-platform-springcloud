package com.dcy.handler;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * 管理员审批
 *
 * @author persie
 * @date 2018/07/19
 */
@Slf4j
public class AdminTaskHandler implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("admin");

        // 1.取得流程Id
        String processDefinitionId = delegateTask.getProcessDefinitionId();
        // 2.取得当前节点key
        String taskKey = delegateTask.getTaskDefinitionKey();
        // 3.取得当前任务的orgId
        String processInId = delegateTask.getProcessInstanceId();
        // 获取业务key
        String businessKey = String.valueOf(delegateTask.getVariable("businessKey"));
        String depaId = "";
        log.info(" processDefinitionId -=- {},taskKey-=-{},processInId-=-{},businessKey-=-{}", processDefinitionId, taskKey, processInId, businessKey);

        /*
        * 1、取业务表数据，得到部门id
          2、根据部门id在取查询role_task关联信息，得到候选人
        * */
        //taskRoleService.userList(depaId, processDefinitionId, taskKey);

    }

}
