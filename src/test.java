
import components.standard.Standard;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.map.Map;
import components.map.Map1L;

interface ConversationLogStandard<T> {
 
    /**
     * Returns a new object with the same <i>dynamic</i> type as {@code this},
     * having an initial value.
     *
     * If the type {@code T} has a no-argument constructor, then the value of
     * the new returned object satisfies the contract of the no-argument
     * constructor for {@code T}. If {@code T} does not have a no-argument
     * constructor, then the value of the new returned object satisfies the
     * contract of the constructor call that was used to initialize {@code this}
     * .
     *
     * @return new object "like" {@code this} with an initial value
     * @ensures is_initial(newInstance)
     */
    T newInstance();
 
    /**
     * Resets {@code this} to an initial value.
     *
     * If the type {@code T} has a no-argument constructor, then {@code this}
     * satisfies the contract of the no-argument constructor for {@code T}. If
     * {@code T} does not have a no-argument constructor, then {@code this}
     * satisfies the contract of the constructor call that was used to
     * initialize {@code #this}.
     *
     * @clears this
     */
    void clear();
 
    /**
     * Sets {@code this} to the incoming value of {@code source}, and resets
     * {@code source} to an initial value; the declaration notwithstanding, the
     * <i>dynamic</i> type of {@code source} must be the same as the
     * <i>dynamic</i> type of {@code this}.
     *
     * If the type {@code T} has a no-argument constructor, then {@code source}
     * satisfies the contract of the no-argument constructor for {@code T}. If
     * {@code T} does not have a no-argument constructor, then {@code source}
     * satisfies the contract of the constructor call that was used to
     * initialize {@code #source}.
     *
     * @param source
     *            object whose value is to be transferred
     * @replaces this
     * @clears source
     * @ensures this = #source
     */
    void transferFrom(T source);
}

interface ConversationLogKernel extends Standard<ConversationLog> {

    // Report number of chat entries in the log.
    int length();


    // Check if the log is empty.
    boolean isEmpty();

    /**
    * Append a chat entry to the log.
    * @param speaker non-null/non-empty
    * @param text non-null
    */
    void append(String speaker, String text);


    /**
    * Undo chat: removes the last entry and reports it as a map with keys
    * "speaker", "text".
    * @return removed entry encoded as a Map<String,String>
    * @requires |this| > 0
    */
    Map<String, String> removeChat();

    // Resets the chat log to empty, this is same with clear().`
    void reset();


    /**
    * Keeps only entries in [start, end) and deletes all other chat.
    * @param start inclusive index
    * @param end exclusive index (0 ≤ start ≤ end ≤ |#this|)
    * @updates this
    * @return this (now containing only the chosen segment)
    */
    ConversationLog segment(int start, int end);
}

interface ConversationLog extends ConversationLogKernel {

    // export/import chat log
    void exportChat(SimpleWriter out);
    void importChat(SimpleReader in);

    // Convert the entire conversation log to a transcript string
    String toTranscript();

    // Search for substring in the chat log, return the list of indices where found
    Sequence<Integer> find(String substring);

    // Get all chats from a specific speaker
    Sequence<String> speakerChat(String speaker);
}
class ConversationLogSecondary implements ConversationLog {

    // Private inner class to represent a chat entry, this would help to store speaker and text together. What I meant is
    // I can stack them together in the sequence. Kind of similar to Map/ Array inside Sequence. But so much better because I can show
    // off new shit i learnt.
    private static class ChatEntry {
        String speaker;
        String text;

        ChatEntry(String speaker, String text) {
            this.speaker = speaker;
            this.text = text;
        }
    }

    // Sequence to hold chat entries
    private Sequence<ChatEntry> entries;

    /**
    * Constructor for ConversationLog1L.
    */
    public ConversationLogSecondary() {
        this.createNewRep();
    }
    private void createNewRep() {
        this.entries = new Sequence1L<>();  
    }

    // Standard interface methods
    @Override
    public ConversationLogSecondary newInstance() {
        return new ConversationLogSecondary();
    }

    @Override
    public void clear() {
        this.entries.clear();
    }

    @Override
    public void transferFrom(ConversationLog source) {
        if (this != source) {
        ConversationLogSecondary that = (ConversationLogSecondary) source;
        this.entries.transferFrom(that.entries);
        }
    }

    // Kernel interface methods
    @Override
    public int length() {
        return this.entries.length();
    }
    @Override
    public boolean isEmpty() {
        return this.entries.length() == 0;
    }
    @Override
    public void append(String speaker, String text) {
        ChatEntry newEntry = new ChatEntry(speaker, text);
        this.entries.add(this.entries.length(), newEntry);
    }
    @Override
    public Map<String, String> removeChat() {
        ChatEntry lastEntry = this.entries.remove(this.entries.length() - 1);
        Map<String, String> removedMap = new Map1L<>();
        removedMap.add("speaker", lastEntry.speaker);
        removedMap.add("text", lastEntry.text);
        return removedMap;
    }
    @Override
    public void reset() {
        // Probably the most LOL method ever
        this.clear();
    }

    @Override
    public ConversationLog segment(int start, int end) {
        Sequence<ChatEntry> newEntries = new Sequence1L<>();
        for (int i = start; i < end; i++) {
            newEntries.add(newEntries.length(), this.entries.entry(i));
        }
        this.entries = newEntries;
        return this;
    }

    // Secondary interface methods
    @Override
    public void exportChat(SimpleWriter out) {
        for (int i = 0; i < this.entries.length(); i++) {
            ChatEntry entry = this.entries.entry(i);
            out.println(entry.speaker + ": " + entry.text);
        }
        out.close();
    }
    @Override
    public void importChat(SimpleReader in) {
        while (!in.atEOS()) {
            String line = in.nextLine();
            int colonIndex = line.indexOf(": ");
            if (colonIndex != -1) {
                String speaker = line.substring(0, colonIndex);
                String text = line.substring(colonIndex + 2);
                this.append(speaker, text);
            }
        }
        in.close();
    }
    @Override
    public String toTranscript() {
        StringBuilder transcript = new StringBuilder();
        for (int i = 0; i < this.entries.length(); i++) {
            ChatEntry entry = this.entries.entry(i);
            transcript.append(entry.speaker).append(": ").append(entry.text).append("\n");
        }
        return transcript.toString();
    }
    @Override
    public Sequence<Integer> find(String substring) {
        Sequence<Integer> indices = new Sequence1L<>();
        for (int i = 0; i < this.entries.length(); i++) {
            ChatEntry entry = this.entries.entry(i);
            if (entry.text.contains(substring)) {
                indices.add(indices.length(), i);
            }
        }
        return indices;
    }
    @Override
    public Sequence<String> speakerChat(String speaker) {
        Sequence<String> chats = new Sequence1L<>();
        for (int i = 0; i < this.entries.length(); i++) {
            ChatEntry entry = this.entries.entry(i);
            if (entry.speaker.equals(speaker)) {
                chats.add(chats.length(), entry.text);
            }
        }
        return chats;
    }

    

}


public class test {
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();

        ConversationLog log = new ConversationLogSecondary();
        out.println("Initially: length=" + log.length() + ", isEmpty=" + log.isEmpty());

        log.append("Six", "Hello");
        log.append("Seven", "Hi there!");
        log.append("Six", "How are you?");
        log.append("Seven", "Doing well, how can I help?");
        out.println("After appends: length=" + log.length() + ", isEmpty=" + log.isEmpty());

        out.println("\nTranscript:");
        out.println(log.toTranscript());

        Sequence<Integer> hits = log.find("Hi");
        out.println("\nIndices containing 'Hi': " + hits);

        Sequence<String> onlySix = log.speakerChat("Six");
        out.println("\nSix-only lines: " + onlySix);

        Map<String, String> undone = log.removeChat();
        out.println("\nUndo last: " + undone);
        out.println("Length now: " + log.length());

        log.segment(0, 2);
        out.println("\nAfter segment(0,2), transcript:");
        out.println(log.toTranscript());

        SimpleWriter fileOut = new SimpleWriter1L("convo.txt");
        log.exportChat(fileOut);  
                

        ConversationLog log2 = new ConversationLogSecondary();
        SimpleReader fileIn = new SimpleReader1L("convo.txt");
        log2.importChat(fileIn);  
        

        out.println("\nReloaded transcript (log2):");
        out.println(log2.toTranscript());

        log2.reset();
        out.println("\nAfter reset, log2 length=" + log2.length() + ", isEmpty=" + log2.isEmpty());

        out.close();
}
}
