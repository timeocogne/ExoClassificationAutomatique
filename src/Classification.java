import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Classification {


    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String id = ligne.substring(3);
                ligne = scanner.nextLine();
                String date = ligne.substring(3);
                ligne = scanner.nextLine();
                String categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                String lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")) {
                        lignes = lignes + '\n' + ligne;
                    }
                }
                Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
                depeches.add(uneDepeche);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }

    public static boolean isInDico(ArrayList<PaireChaineEntier> dico, String word){
        int i = 0;
        while(i < dico.size() && !dico.get(i).getChaine().equalsIgnoreCase(word)){
            i++;
        }
        return !(i == dico.size());
    }

    public static void sortLexique(ArrayList<PaireChaineEntier> lexique){
        //Trie bulle
        for(int i = 0; i < lexique.size(); i++){
            for(int j = lexique.size() - 1; j > i + 1; j--){
                if(lexique.get(j).compareTo(lexique.get(j-1)) < 0){
                    PaireChaineEntier tmp = lexique.get(j);
                    lexique.set(j, lexique.get(j-1));
                    lexique.set(j-1, tmp);
                }
            }
        }

    }

    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        for(Depeche depeche : depeches){
            //On prend que les dépèches de la catégorie qu'on traite
            if(depeche.getCategorie().equals(categorie)){

                //On traite les mots un à un
                for(String word : depeche.getMots()){
                    //Si le mot n'est pas déjà dedans alors on l'ajoute
                    if(!isInDico(resultat, word)) {
                        resultat.add(new PaireChaineEntier(word, 0));
                    }
                }
            }
        }

        return resultat;

    }


    public static boolean rechercheDichotomique(ArrayList<String> vString, String str){
        int min = 0;
        int max = vString.size() - 1;

        while(min < max){
            int m = (min + max) / 2;

            if(vString.get(m).compareTo(str) < 0){
                min = m + 1;
            }
            else{
                max = m;
            }
        }

        return vString.get(min).equals(str);

    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        for(PaireChaineEntier motDico : dictionnaire){
            //Pour chaque mots on regarde on regarde le nombre d'occurence dans toutes les dépêches
            for(Depeche depeche : depeches){
                int i = 0;
                /*while(i < depeche.getMots().size() && depeche.getMots().get(i).compareTo(motDico.getChaine()) < 0){
                    i++;
                }
                if(i < depeche.getMots().size() && depeche.getMots().get(i).compareTo(motDico.getChaine()) == 0) {*/
                if(rechercheDichotomique(depeche.getMots(), motDico.getChaine())){
                    //Si on trouve le même mot on regarde si c'est la même catégorie ou pas
                    if (depeche.getCategorie().equals(categorie)) {
                        motDico.setEntier(motDico.getEntier() + 1);
                    } else {
                        motDico.setEntier(motDico.getEntier() - 1);
                    }
                }
            }
        }
    }

    public static int poidsPourScore(int score) {
        return score / 10 + 1;
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        ArrayList<PaireChaineEntier> dico = Classification.initDico(depeches, categorie);
        Classification.calculScores(depeches, categorie, dico);

        try {
            FileWriter file = new FileWriter(nomFichier);

            for (PaireChaineEntier wordDico : dico) {
                if(wordDico.getEntier() > 0) {
                    file.write(wordDico.getChaine() + ":" + Math.min(3, poidsPourScore(wordDico.getEntier())) + "\n");
                }
            }

            file.close();
        }
        catch (IOException e){
            System.out.println("Erreur d'écriture du fichier");
        }

    }

    public static void main(String[] args) {

        //Chargement des dépêches en mémoire
        System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("./test.txt");


        ArrayList<Categorie> categories = new ArrayList<>(Arrays.asList(
                new Categorie("CULTURE"),
                new Categorie("ECONOMIE"),
                new Categorie("POLITIQUE"),
                new Categorie("SPORTS"),
                new Categorie("ENVIRONNEMENT-SCIENCES")
        ));

        //Initialisation des lexiques
        for(Categorie c : categories){
            c.initLexique(c.getNom() + ".gen");
        }

        long startTime = System.currentTimeMillis();
        Categorie.classementDepeche(depeches, categories, "result.txt");
        System.out.println("Done ! (" + (System.currentTimeMillis() - startTime) + "ms)");

        /*
        long startTime = System.currentTimeMillis();
        Classification.generationLexique(depeches, "SPORTS", "./Lexiques/SPORTS.gen");
        Classification.generationLexique(depeches, "CULTURE", "./Lexiques/CULTURE.gen");
        Classification.generationLexique(depeches, "ECONOMIE", "./Lexiques/ECONOMIE.gen");
        Classification.generationLexique(depeches, "POLITIQUE", "./Lexiques/POLITIQUE.gen");
        Classification.generationLexique(depeches, "ENVIRONNEMENT-SCIENCES", "./Lexiques/ENVIRONNEMENT-SCIENCES.gen");



        System.out.println("Done ! (" + (System.currentTimeMillis() - startTime) + "ms)");*/
        /*for (int i = 0; i < depeches.size(); i++) {
            depeches.get(i).afficher();
        }*/

    }


}

