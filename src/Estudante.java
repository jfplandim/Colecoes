public class Estudante {
    private int id;
    private String nome;


    public Estudante(int id, String nome){
        this.id=id;
        this.nome=nome;
    }

    public int getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    @Override
    public String toString() {
        return id + " - " + nome;
    }
}
