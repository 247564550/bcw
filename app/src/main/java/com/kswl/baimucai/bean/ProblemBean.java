package com.kswl.baimucai.bean;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc
 * @date 2017-2017/2/21-15:09
 */

public class ProblemBean {

    private String id;

    private String problem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    @Override
    public String toString() {
        return problem == null ? "" : problem;
    }
}
