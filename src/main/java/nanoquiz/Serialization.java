package nanoquiz;

import nanoquiz.util.Pair;
import nanoquiz.util.IOUtil;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;

public abstract class Serialization {
    public static void loadKnowledge() {
        File knowledgeFile = Main.workdir.resolve("knowledge").toFile();
        if(!knowledgeFile.exists()) return;
        BufferedInputStream reader = null;
        HashMap<Integer, Pair<Long, Float>> data = new HashMap<>();
        try {
            reader = new BufferedInputStream(new FileInputStream(knowledgeFile));
            while (true) {
                int id;
                long time;
                float score;
                try {
                    id = IOUtil.byteArrayToInt(IOUtil.readBytesOrWait(reader, 4));
                    time = IOUtil.byteArrayToLong(IOUtil.readBytesOrWait(reader, 8));
                    score = Float.intBitsToFloat(IOUtil.byteArrayToInt(IOUtil.readBytesOrWait(reader, 4)));
                } catch(EOFException e) {
                    break;
                }
                data.put(id, new Pair<Long, Float>(time, score));
            }
        } catch(IOException e) {
            e.printStackTrace();	
            return;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        for(Question question : Main.questions) {
            Pair<Long, Float> thisData = data.get(question.question.hashCode());
            if (thisData == null) {
                continue;
            }
            question.lastQuizzed = thisData.first();
            question.score = thisData.second();
        }
    }

    public static void saveKnowledge() {
        File knowledgeFile = Main.workdir.resolve("knowledge").toFile();
        BufferedOutputStream writer = null;
        try {
            writer = new BufferedOutputStream(new FileOutputStream(knowledgeFile));
            for (Question question : Main.questions) {
                writer.write(IOUtil.intToByteArray(question.question.hashCode()));
                writer.write(IOUtil.longToByteArray(question.lastQuizzed));
                writer.write(IOUtil.intToByteArray(Float.floatToIntBits(question.score)));
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
