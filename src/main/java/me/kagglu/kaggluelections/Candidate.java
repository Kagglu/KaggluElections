package me.kagglu.kaggluelections;

public class Candidate {

    private int voteCount;
    private String name;

    public Candidate(String name) {
        this.name = name;
    }

    public Candidate(String name, int count) {
        this.name = name;
        this.voteCount = count;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getName() {
        return name;
    }

    public void setVoteCount(int voteCount) {
        if (voteCount < 0) {
            this.voteCount = 0;
        } else {
            this.voteCount = voteCount;
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}
