package test.autoparams.customization;

@SuppressWarnings("LombokSetterMayBeUsed")
public class Worker extends User {

    private String teamName;
    private int activeWorks;
    private int closedWorks;

    public String getTeamName() {
        return teamName;
    }

    public int getActiveWorks() {
        return activeWorks;
    }

    public void setActiveWorks(int activeWorks) {
        this.activeWorks = activeWorks;
    }

    public int getClosedWorks() {
        return closedWorks;
    }

    public void setClosedWorks(int closedWorks) {
        this.closedWorks = closedWorks;
    }
}
