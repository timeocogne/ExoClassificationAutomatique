public class PaireChaineEntier implements Comparable<PaireChaineEntier>{

    private String chaine;
    private int entier;

    public PaireChaineEntier(String chaine, int entier){
        this.chaine = chaine;
        this.entier = entier;
    }

    public String getChaine(){
        return this.chaine;
    }

    public int getEntier(){
        return this.entier;
    }

    public void setEntier(int entier){
        this.entier = entier;
    }

    @Override
    public String toString() {
        return chaine + "(" + entier + ")\n";
    }

    @Override
    public int compareTo(PaireChaineEntier o) {
        return this.entier - o.getEntier();
    }
}
