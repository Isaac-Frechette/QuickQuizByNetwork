package Managers;

import Models.Question;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QuestionManager {

    public Question getByID(int id) {
        Question question = new Question();
        String[] temp = new String[8];

        try {
            String line;
            FileInputStream fstream = new FileInputStream("Questions.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while ((line = br.readLine()) != null) {
                if (Integer.toString(id).equals(line)) {
                    temp[0] = line;
                    for (int i = 1; i < 8; i++) {
                        temp[i] = br.readLine();
                    }
                    question = new Question(Integer.parseInt(temp[0]), temp[1], temp[2], temp[3], temp[4], temp[5], temp[6], temp[7]);
                }
            }

            br.close();
            in.close();
            fstream.close();

        } catch (Exception e) {
            System.err.println("Error Reading File: " + e.getMessage());
        }
        return question;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        String[] temp = new String[8];

        try {
            int lineCount = 0;
            String line;

            FileInputStream fstream = new FileInputStream("Questions.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while ((line = br.readLine()) != null) {
                temp[lineCount] = line;
                lineCount++;

                if (lineCount == 8) {
                    Question tempQuestion = new Question(Integer.parseInt(temp[0]), temp[1], temp[2], temp[3], temp[4], temp[5], temp[6], temp[7]);
                    questions.add(tempQuestion);
                    lineCount = 0;
                }
            }

            br.close();
            in.close();
            fstream.close();

        } catch (Exception e) {
            System.err.println("Error Reading File: " + e.getMessage());
        }
        return questions;
    }
}
