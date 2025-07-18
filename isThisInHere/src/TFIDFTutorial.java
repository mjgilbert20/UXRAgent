// TFIDFTutorial.java (Refactored to use GloVe-based Semantic Search)
// Michael

import java.io.*;
import java.util.*;
import java.nio.file.*;

public class TFIDFTutorial {

    private Map<String, float[]> gloveEmbeddings;
    private List<String> documents;
    private List<float[]> documentVectors;

    public TFIDFTutorial(List<String> docs, String glovePath) throws IOException {
        this.documents = docs;
        this.gloveEmbeddings = loadGloveEmbeddings(glovePath);
        this.documentVectors = new ArrayList<>();

        for (String doc : documents) {
            float[] vector = getSentenceEmbedding(doc);
            documentVectors.add(vector);
        }
    }

    private Map<String, float[]> loadGloveEmbeddings(String filePath) throws IOException {
        Map<String, float[]> embeddings = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String word = parts[0];
                float[] vector = new float[parts.length - 1];
                for (int i = 1; i < parts.length; i++) {
                    vector[i - 1] = Float.parseFloat(parts[i]);
                }
                embeddings.put(word, vector);
            }
        }
        return embeddings;
    }

    private float[] getSentenceEmbedding(String sentence) {
        String[] tokens = sentence.toLowerCase().replaceAll("[^a-z0-9 ]", "").split(" ");
        int vectorSize = gloveEmbeddings.values().iterator().next().length;
        float[] avgVector = new float[vectorSize];
        int count = 0;

        for (String token : tokens) {
            float[] vec = gloveEmbeddings.get(token);
            if (vec != null) {
                for (int i = 0; i < vectorSize; i++) {
                    avgVector[i] += vec[i];
                }
                count++;
            }
        }

        if (count == 0) return new float[vectorSize];
        for (int i = 0; i < vectorSize; i++) {
            avgVector[i] /= count;
        }
        return avgVector;
    }

    public List<String> search(String query, int topK) {
        float[] queryVector = getSentenceEmbedding(query);
        PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(Map.Entry.comparingByValue());

        for (int i = 0; i < documents.size(); i++) {
            double similarity = cosineSimilarity(queryVector, documentVectors.get(i));
            pq.offer(new AbstractMap.SimpleEntry<>(documents.get(i), similarity));
            if (pq.size() > topK) pq.poll();
        }

        List<String> results = new ArrayList<>();
        while (!pq.isEmpty()) results.add(0, pq.poll().getKey());
        return results;
    }

    private double cosineSimilarity(float[] vec1, float[] vec2) {
        double dot = 0.0, norm1 = 0.0, norm2 = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            dot += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }
        return (norm1 == 0 || norm2 == 0) ? 0 : dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public static void main(String[] args) throws IOException {
        List<String> documents = Arrays.asList(
                "What is semantic text similarity?",
                "TF-IDF explained with examples",
                "Using GloVe for document understanding",
                "Comparing BERT and GloVe embeddings",
                "Natural language inference and semantic roles"
        );

        String gloveFile = "isThisInHere/glove.6B.300d.txt"; // Update with actual path
        TFIDFTutorial searchEngine = new TFIDFTutorial(documents, gloveFile);

        List<String> results = searchEngine.search("semantic understanding of sentences", 3);
        System.out.println("Top Matches:");
        for (String result : results) {
            System.out.println("- " + result);
        }
    }
}