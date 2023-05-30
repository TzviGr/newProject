package org.example;

public class Reminder {
    private String what;
    private int when;
    private boolean sent;
    private boolean completed;


    public String getWhat() {
        return what;
    }
    public int getWhen() {
        return when;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public void setWhen(int when) {
        this.when = when;
    }
    public void decrementTime(){
        this.when--;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
