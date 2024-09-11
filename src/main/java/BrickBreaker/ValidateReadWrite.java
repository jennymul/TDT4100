package BrickBreaker;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class ValidateReadWrite {

    public boolean validateName(String name) {
        if (name.contains(" ")) {
            System.out.println("Navn kan ikke inneholde mellomrom");
            return true;
        } else if (name == "") {
            System.out.println("Navnefelt kan ikke være tomt");
            return true;
        }
        return false;
    }

    public void lagreVerdi(String name, int score, String filePath) {
        if (!validateName(name)) {
            try {
                FileWriter scoreBoardWriter = new FileWriter(filePath, true);
                scoreBoardWriter.write(name + " " + Integer.toString(score) + "\n");
                scoreBoardWriter.close();
                System.out.println("Lagring vellykket");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Lagring feilet");
            }

        }
    }

    public String leseScoreboard() {
        String scoreFromFile = null;
        try {
            File scores = new File("ScoreBoard.txt");
            Scanner reader = new Scanner(scores);
            while (reader.hasNextLine()) {
                scoreFromFile = reader.nextLine();
                BrickBreakerController.setScoreBoardOversikt(scoreFromFile);
                System.out.println(scoreFromFile);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fil ikke funnet");
        }
        return scoreFromFile;
    }
}
