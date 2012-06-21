package com.danikula.android.garden.task;

/*package private*/class TaskInfo {

    public int taskId;

    public int action;

    TaskInfo(int taskId, int action) {
        this.taskId = taskId;
        this.action = action;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + action;
        result = prime * result + taskId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaskInfo other = (TaskInfo) obj;
        if (action != other.action)
            return false;
        if (taskId != other.taskId)
            return false;
        return true;
    }

}
