import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class HTMLRead implements ActionListener {
    List<String> resultList = new ArrayList<>();

    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;

    private JFrame mainFrame;
    private JLabel header;
    private JLabel urlInfo;
    private JLabel keywordInfo;
    private JPanel buttons;
    private JPanel resultDisplay;
    private JPanel resultDisplayInside;
    private JPanel inputFields;
    private JPanel inputFieldsInside;
    private JMenuBar menu;
    private JTextArea urlInput;
    private JTextArea keywordInput;
    private JTextArea resultOutput;
    private JScrollPane resultScrollPane;
    private String urlText;
    private String keywordText;

    public HTMLRead() {
        createInterface();
    }

    private void createInterface() {
        mainFrame = new JFrame("HTML Reader");
        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        mainFrame.setLayout(new GridLayout(2, 2));

        menu = new JMenuBar();

        urlInput = new JTextArea();
        urlInput.setBorder(BorderFactory.createTitledBorder("Enter URL:"));
        keywordInput = new JTextArea();
        keywordInput.setBorder(BorderFactory.createTitledBorder("Enter Keyword:"));
        inputFields = new JPanel(new BorderLayout());
        inputFieldsInside = new JPanel(new GridLayout(2, 2));
        inputFieldsInside.add(urlInput);
        inputFieldsInside.add(keywordInput);
        buttons = new JPanel(new FlowLayout());
        inputFields.add(inputFieldsInside, BorderLayout.CENTER);
        inputFields.add(buttons, BorderLayout.SOUTH);

        header = new JLabel("", JLabel.CENTER);
        urlInfo = new JLabel("", JLabel.CENTER);
        keywordInfo = new JLabel("", JLabel.CENTER);
        resultOutput = new JTextArea();
        resultScrollPane = new JScrollPane(resultOutput);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Output:"));
        resultScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        resultDisplayInside = new JPanel(new GridLayout(2, 2));
        resultDisplayInside.add(urlInfo);
        resultDisplayInside.add(keywordInfo);
        resultDisplay = new JPanel(new BorderLayout());
        resultDisplay.add(resultDisplayInside, BorderLayout.NORTH);
        resultDisplay.add(resultScrollPane, BorderLayout.CENTER);

        mainFrame.add(menu);
        mainFrame.setJMenuBar(menu);
        mainFrame.add(inputFields);
        mainFrame.add(resultDisplay);
        mainFrame.setVisible(true);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

    }

    private void displayHTML() {
        header.setText("Action");

        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchButton.setActionCommand("Search");
        clearButton.setActionCommand("Clear");

        searchButton.addActionListener(new ButtonHandler());
        clearButton.addActionListener(new ButtonHandler());

        buttons.add(searchButton);
        buttons.add(clearButton);

        mainFrame.setVisible(true);

    }

    public static void main(String[] args) {
        HTMLRead htmlRead = new HTMLRead();
        htmlRead.displayHTML();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            if (action.equals("Search")) {
                urlText = urlInput.getText();
                urlInfo.setText(urlInput.getText() + " is entered as the URL");
                keywordText = keywordInput.getText();
                keywordInfo.setText(keywordInput.getText() + " is entered as the Keyword");

                resultList = urlGetter(urlText, keywordText);

                for (String output : resultList) {
                    resultOutput.append(output + "\n");
                }
            } else if (action.equals("Clear")) {
                resultList.clear();
                resultOutput.setText("Clear Button clicked.");
                urlInput.setText("");
                keywordInput.setText("");
                urlInfo.setText("");
            }
        }
    }

    private ArrayList<String> urlGetter(String urlInput, String keyword) {
        ArrayList<String> urls = new ArrayList<>();

        // https://www.milton.edu/

        try {
            URL url = new URL(urlInput);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream())
            );
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("href")) {
                    try {
                        int beginIndex = line.indexOf("http");
                        int endIndexDouble = line.indexOf("\"", beginIndex);
                        int endIndexSingle = line.indexOf("'", beginIndex);

                        String link;

                        if (endIndexSingle == -1) {
                            link = line.substring(beginIndex, endIndexDouble);
                        } else if (endIndexDouble == -1) {
                            link = line.substring(beginIndex, endIndexSingle);
                        } else {
                            continue;
                        }


                        if (link.contains(keyword)) {
                            urls.add(link);
                            System.out.println(link);
                        }

                    } catch (Exception e) {
                    }
                }
            }
            reader.close();

        } catch (Exception ex) {

        }

        return urls;
    }
}