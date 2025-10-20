import java.util.Objects;

public class Disciplina {
    private String codigo;
    private String nome;

    public Disciplina(String codigo, String nome){
        this.codigo=codigo;
        this.nome=nome;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public String getNome() {
        return this.nome;
    }

    //comparar se a materia ja existe
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disciplina that = (Disciplina) o;
        return Objects.equals(codigo, that.codigo);
    }

    //define o hashCode de cada materia
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

   @Override
    public String toString(){
        return codigo + ", " ;
    }
}