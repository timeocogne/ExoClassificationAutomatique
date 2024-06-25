import java.util.ArrayList;

public class UtilitairePaireChaineEntier {


    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {

        int i = 0;
        while(i < listePaires.size() && !listePaires.get(i).getChaine().equalsIgnoreCase(chaine)){
            i++;
        }

        if(i == listePaires.size())
            return -1;

        return i;

    }

    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {

        int i = 0;
        while (i < listePaires.size() && !listePaires.get(i).getChaine().equalsIgnoreCase(chaine)){
            i++;
        }

        if(i == listePaires.size()) return 0; //Si la chaine n'est pas présente on retourne 0
        return listePaires.get(i).getEntier();

    }

    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        int max = listePaires.get(0).getEntier();
        String chaine = listePaires.get(0).getChaine();

        int i = 1;
        while(i < listePaires.size()){
            if(listePaires.get(i).getEntier() > max){
                max = listePaires.get(i).getEntier();
                chaine = listePaires.get(i).getChaine();
            }
            i++;
        }

        return chaine;
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        int somme = 0;

        int i = 0;
        while(i < listePaires.size()){
            somme += listePaires.get(i).getEntier();
            i++;
        }

        return somme / listePaires.size();
    }

}
