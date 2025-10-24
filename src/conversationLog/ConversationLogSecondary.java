package conversationLog;

import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.simplereader.SimpleReader;
import components.simplewriter.SimpleWriter;

/**
 * Sequence-backed implementation of ConversationLog .
 */
// I don't use abstract class here because I want to implement all methods
// in this class directly.
public class ConversationLogSecondary implements ConversationLog {

    // Private inner class to represent a chat entry, this would help to store
    // speaker and text together. What I meant is
    // I can stack them together in the sequence. Kind of similar to Map/ Array
    // inside Sequence. But so much better because I can show
    // off new shit i learnt.

    /**
     *
     * Constructs a chat entry with the given speaker and text.
     *
     * @param speaker
     *            the speaker label (non-{@code null}, non-empty)
     * @param text
     *            the message text (non-{@code null})
     */
    private static class ChatEntry {

        String speaker;
        String text;

        /**
         * Constructor: initialize a new ChatEntry.
         *
         * @param speaker
         * @param text
         */
        ChatEntry(String speaker, String text) {
            this.speaker = speaker;
            this.text = text;
        }
    }

    /**
     * representation of the conversation log.
     */
    private Sequence<ChatEntry> entries;

    /**
     * Constructor for ConversationLog1L.
     */
    public ConversationLogSecondary() {
        this.createNewRep();
    }

    /**
     * Initializes the representation to an empty sequence.
     *
     * @updates this.entries
     * @ensures {@code entries.length() = 0}
     *
     */
    private void createNewRep() {
        this.entries = new Sequence1L<>();
    }

    // Standard interface methods
    /**
     * Returns a new object with the same <i>dynamic</i> type as {@code this},
     * having an initial value.
     */
    @Override
    public ConversationLogSecondary newInstance() {
        return new ConversationLogSecondary();
    }

    /**
     * Resets {@code this} to an initial value.
     */
    @Override
    public void clear() {
        this.entries.clear();
    }

    /**
     * Sets {@code this} to the incoming value of {@code source}, and resets
     * {@code source} to an initial value; the declaration notwithstanding, the
     * <i>dynamic</i> type of {@code source} must be the same as the
     * <i>dynamic</i> type of {@code this}.
     */
    @Override
    public void transferFrom(ConversationLog source) {
        if (this != source) {
            ConversationLogSecondary that = (ConversationLogSecondary) source;
            this.entries.transferFrom(that.entries);
        }
    }

    // Kernel interface methods
    /**
     * Report the number of chat entries in the log.
     *
     * @return the number of chat entries
     */
    @Override
    public int length() {
        return this.entries.length();
    }

    /**
     * Check if the log is empty.
     *
     * @return true if the log is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return this.entries.length() == 0;
    }

    /**
     * Append a chat entry to the log.
     *
     * @param speaker
     *            non-null/non-empty
     * @param text
     *            non-null
     */
    @Override
    public void append(String speaker, String text) {
        ChatEntry newEntry = new ChatEntry(speaker, text);
        this.entries.add(this.entries.length(), newEntry);
    }

    /**
     * Undo chat: removes the last entry and reports it as a map with keys
     * "speaker", "text".
     *
     * @return removed entry encoded as a Map<String,String>
     * @requires |this| > 0
     */
    @Override
    public Map<String, String> removeChat() {
        ChatEntry lastEntry = this.entries.remove(this.entries.length() - 1);
        Map<String, String> removedMap = new Map1L<>();
        removedMap.add("speaker", lastEntry.speaker);
        removedMap.add("text", lastEntry.text);
        return removedMap;
    }

    /**
     * Reset the conversation log to empty.
     *
     * @clear this
     */
    @Override
    public void reset() {
        // Probably the most LOL method ever
        this.clear();
    }

    /**
     * Keeps only entries in [start, end) and deletes all other chat.
     *
     * @param start
     *            inclusive index
     * @param end
     *            exclusive index (0 ≤ start ≤ end ≤ |#this|)
     * @updates this
     * @return this (now containing only the chosen segment)
     */
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
    /**
     * Export the chat log to a SimpleWriter.
     *
     * @param out
     *            the SimpleWriter to write to
     */
    @Override
    public void exportChat(SimpleWriter out) {
        for (int i = 0; i < this.entries.length(); i++) {
            ChatEntry entry = this.entries.entry(i);
            out.println(entry.speaker + ": " + entry.text);
        }
        out.close();
    }

    /**
     * Import the chat log from a SimpleReader.
     *
     * @param in
     *            the SimpleReader to read from
     */
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

    /**
     * Convert the entire conversation log to a transcript string.
     *
     * @return the transcript string
     */
    @Override
    public String toTranscript() {
        StringBuilder transcript = new StringBuilder();
        for (int i = 0; i < this.entries.length(); i++) {
            ChatEntry entry = this.entries.entry(i);
            transcript.append(entry.speaker).append(": ").append(entry.text)
                    .append("\n");
        }
        return transcript.toString();
    }

    /**
     * Search for a substring in the chat log and return the list of indices
     * where found.
     *
     * @param substring
     *            the substring to search for
     * @return a sequence of indices where the substring is found
     */
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

    /**
     * Get all chats from a specific speaker.
     *
     * @param speaker
     *            the speaker whose chats to retrieve
     * @return a sequence of chats from the specified speaker
     */
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
