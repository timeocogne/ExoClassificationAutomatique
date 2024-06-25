import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Categorie {

    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private HashMap<String, Integer> lexique; //le lexique de la catégorie


    // constructeur
    public Categorie(String nom) {
        this.nom = nom;
        this.lexique = new HashMap<>();
    }


    public String getNom() {
        return nom;
    }


    public HashMap<String, Integer> getLexique() {
        return lexique;
    }


    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public void initLexique(String nomFichier) {
        try {
            FileInputStream lexique_file = new FileInputStream("./Lexiques/" + nomFichier);

            Scanner scanner = new Scanner(lexique_file);

            while(scanner.hasNextLine()){

                String line = scanner.nextLine();

                try {
                    String word = line.substring(0, line.length() - 2);
                    int poids = Integer.parseInt(line.substring(line.length() - 1));
                    this.lexique.put(word, poids);
                }
                catch (NumberFormatException e){
                    System.err.println("Erreur de format dans le lexique");
                }
                catch (IndexOutOfBoundsException e){
                    System.err.println("Erreur de format dans le lexique");
                }
            }
        }
        catch (FileNotFoundException e){
            System.err.println("Le fichier de lexique de la catégorie '" + this.nom + "' est introuvable !");
        }

    }


    //calcul du score d'une dépêche pour la catégorie
    public int score(Depeche d) {

        int score = 0;

        for(String mot : d.getMots()){
            if(this.lexique.containsKey(mot))
                score += this.lexique.get(mot);
        }

        return score;
    }

    public static void classementDepeche(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFicher){

        try {
            FileWriter file = new FileWriter(nomFicher);
            ArrayList<PaireChaineEntier> precision = new ArrayList<>();

            for (Categorie categorie : categories) {
                precision.add(new PaireChaineEntier(categorie.getNom(), 0));
            }

            for (Depeche depeche : depeches) {
                ArrayList<PaireChaineEntier> resultat = new ArrayList<>();

                for (Categorie categorie : categories) {
                    int score = categorie.score(depeche);
                    resultat.add(new PaireChaineEntier(categorie.getNom(), score));
                }

                String categorie_probable = UtilitairePaireChaineEntier.chaineMax(resultat);

                //On regarde si la catégorie devinée est la bonne
                if (categorie_probable.equals(depeche.getCategorie())) {
                    int index = UtilitairePaireChaineEntier.indicePourChaine(precision, depeche.getCategorie());
                    precision.get(index).setEntier(precision.get(index).getEntier() + 1);
                }

                int index = UtilitairePaireChaineEntier.indicePourChaine(resultat, categorie_probable);
                //Ajout du résultat dans le fichier
                file.write(depeche.getId() + ":" + categorie_probable + " ("+ resultat.get(index).getEntier() +")\n");

            }

            file.write("\n\n");

            //On affiche la précision pour chaque catégorie
            for(PaireChaineEntier categorie_prec : precision){
                file.write(categorie_prec.getChaine() + " : " + categorie_prec.getEntier() + "% \n");
            }

            file.write("Moyenne : " + UtilitairePaireChaineEntier.moyenne(precision) + "% \n");

            file.close();
        }
        catch (IOException e){
            System.err.println("Erreur lors de l'écriture du fichier !");
        }
    }


}
