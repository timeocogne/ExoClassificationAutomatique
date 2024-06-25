import java.util.ArrayList;
import java.util.Arrays;

public class Depeche {

    private String id;
    private String date;
    private String categorie;
    private String contenu;
    private ArrayList<String> mots;


    public Depeche(String id, String date, String categorie, String contenu) {
        this.id = id;
        this.date = date;
        this.categorie = categorie;
        this.contenu = contenu;
        this.mots = decoupeEnMots(contenu);
    }

    public static int getIndiceString(ArrayList<String> vString, String str){
        int i = 0;
        while(i < vString.size() && str.compareTo(vString.get(i)) > 0){
            i++;
        }
        return i;
    }

    private ArrayList<String> decoupeEnMots(String contenu) {
        String chaine = contenu.toLowerCase();
        chaine = chaine.replace('\n', ' ');
        chaine = chaine.replace('.', ' ');
        chaine = chaine.replace(',', ' ');
        chaine = chaine.replace('\'', ' ');
        chaine = chaine.replace('\"', ' ');
        chaine = chaine.replace('(', ' ');
        chaine = chaine.replace(')', ' ');
        String[] tabchaine = chaine.split(" ");
        ArrayList<String> resultat = new ArrayList<String>();

        for (int i = 0; i < tabchaine.length; i++) {
            if (!tabchaine[i].equals("")) {
                //On insert dans le tableau de façon à que celui ci soit trié
                int indice = getIndiceString(resultat, tabchaine[i]);

                resultat.add("");
                for(int j = resultat.size() - 1; j > indice; j--){
                    resultat.set(j, resultat.get(j - 1));
                }
                resultat.set(indice, tabchaine[i]);

                //resultat.add(tabchaine[i]);
            }
        }
        return resultat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public ArrayList<String> getMots() {
        return mots;
    }

    public void setMots(ArrayList<String> mots) {
        this.mots = mots;
    }

    public void afficher() {
        System.out.println("---------------------");
        System.out.println("depêche " + id);
        System.out.println("date:" + date);
        System.out.println("catégorie:" + categorie);
        System.out.println(contenu);
        System.out.println();
        System.out.println("---------------------");
    }

}

