package dtos;

import java.util.ArrayList;

public class MyJokeDTO {
    private String id;
    private ArrayList<String> jokes;

    public MyJokeDTO(ChuckDTO cd, DadDTO dd) {
        this.id = cd.getId()+"_"+dd.getId();
        jokes = new ArrayList<>();
        addJoke(cd.getValue());
        addJoke(dd.getJoke());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getJokes() {
        return jokes;
    }

    public void addJoke(String joke) {
        this.jokes.add(joke);
    }

    @Override
    public String toString() {
        return "MyJokeDTO{" +
                "id='" + id + '\'' +
                ", jokes=" + jokes +
                '}';
    }
}
