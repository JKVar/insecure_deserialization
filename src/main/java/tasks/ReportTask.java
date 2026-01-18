package tasks;

import model.Task;

public class ReportTask implements Task {

    private String reportName;

    public ReportTask() { this.reportName = ""; }

    public ReportTask(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public void execute() {
        System.out.println("Generating report: " + reportName);
    }

    @Override
    public void setParam(Object param) {
        this.reportName = (String) param;
    }
}

