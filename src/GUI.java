import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class GUI {
    // Opretter vores frame, panel og label instanser + alle knapper
    JFrame frame = new JFrame("Stream World");
    JPanel mainPanel = new JPanel();
    JPanel navPanel = new JPanel();
    JPanel forsidePanel = new JPanel();
    JPanel seriePanel = new JPanel();
    JPanel filmPanel = new JPanel();
    JPanel minListePanel = new JPanel();
    JPanel watchPanel = new JPanel();
    JPanel searchPanel = new JPanel();
    JPanel filterPanel = new JPanel();
//  Arraliste af paneler. Det skal være i den samme rækkefølge som arrScrollPane
//  navpanel er ikke inkluderet, der den altid skal vises.
    ArrayList<JPanel> arrPanel = new ArrayList<>(Arrays.asList(forsidePanel,seriePanel,filmPanel,minListePanel,searchPanel,watchPanel));

    //scroll bar paneler
    protected JScrollPane forsideScroll, filmScroll ,serierScroll , minListeScroll , watchScroll , searchScroll, filterScroll,playMedieScroll ;

    //Array liste af JScrollPane
    //Skal være samme rækkefølge som arrPanel
    ArrayList<JScrollPane> arrScrollPane = new ArrayList<>(Arrays.asList(forsideScroll,serierScroll,filmScroll, minListeScroll, searchScroll, watchScroll));
    // Array af alle genre
    String[] genre = {"Vælg", "Action", "Adventure", "Animation", "Biography", "Comedy", "Crime", "Documentary"
                        , "Drama", "Film-Noir", "Family", "Fantasy", "History", "Horror", "Music",
                        "Musical", "Mystery", "Romance", "Sci-fi", "Sport", "Talk-show", "Thriller", "War", "Western"};

    HashMap<JScrollPane,JPanel> hashPanelScroll = new HashMap<>();
    protected JScrollPane activePanel; // Nuværende scroll pane som bliver vist. Bliver brugt i display metoden

    //Navigations komponenter
    protected JButton logoBtn; //Stream World logo knap
    protected JButton forsideBtn = new JButton("Forside");
    protected JButton serierBtn = new JButton("Serier");
    protected JButton filmBtn = new JButton("Film");
    protected JTextField searchField = new JTextField(); //Søge felt
    protected JComboBox filterGenre = new JComboBox(genre);
    protected JButton minListeBtn = new JButton("Min Liste");
    //Array liste af JButton knapper fra navigations
    protected  ArrayList<JButton> arrButtons = new ArrayList<>(Arrays.asList(forsideBtn,serierBtn,filmBtn,minListeBtn));

    //    Array list af medie(forsider/plakater) som knapper
    protected ArrayList<JButton> arrBtnSerier = new ArrayList<>(), arrBtnFilm = new ArrayList<>(), arrBtnAlle = new ArrayList<>();

    //Array liste af både film og serier objekter
    protected ArrayList<Medie> arrAlle = new ArrayList<>();

    protected JPanel infoPanel = new JPanel(); //Den skal indeholde info om mediet
    protected JPanel playMediePanel = new JPanel();
    protected JPanel spilSeriePanel = new JPanel();

    JComboBox seasonComboBox;
    JComboBox episodeComboBox;

    //Konstruktør som kører når vi kalder GUI() fra Main
    public GUI (ArrayList<Medie> arrFilm,ArrayList<Medie> arrSerier ){
        //      Adder array film og array serier i arraylsite alle
        arrAlle.addAll(arrFilm);
        arrAlle.addAll(arrSerier);
        Collections.shuffle(arrAlle); //Blander arrayliste i en tilfældig rækkefølge
//    readFile metoden læser alle film og serier fra /forsider/ mappen
        readFile(arrFilm,arrBtnFilm, filmPanel);
        readFile(arrSerier,arrBtnSerier, seriePanel);
        //Den her metode vil læse både serier og fil, også display det i forside panel
        readFile(arrAlle,arrBtnAlle,forsidePanel);

        //Den her try catch læser logoet af Stream World og indsætter det i logoBtn

        // Vi kalder alle vores metoder i vores konstruktør så det er mere organiseret
        panelScroll();
        navButtons();
        ActionListener();
        frameMethods();

    }
    public void panelScroll() {
        /* Vi har alle vores paneler.
           Det er her vi ændrer og definerer hvordan vores paneler skal se ud */

//      Sætter mainPanel til at være BorderLayout (North,East,West,Sout,Center)
        mainPanel.setLayout(new BorderLayout());
//      Her definere hvordan vores navigations panel ser ud
        navPanel.setBackground(Color.black);
        navPanel.setLayout(new FlowLayout());
        navPanel.setBounds(0,0,1920,100);
        navPanel.setVisible(true);

//        Kører arrpanel igennem, og sætter alle panelerne til grid layout med 20 rækker og 5 kolonner
        for(JPanel jp : arrPanel){
            jp.setLayout(new GridLayout(20,5));
            jp.setBackground(new Color(32,32,32));
        }
        //Forside skal have 40 rækker, da der er 200 medier
        forsidePanel.setLayout(new GridLayout(40,5));

        //Watch panel skal også have en anden layout, der den skal vise
        //info om mediet, og have en knap til min liste og afspilning
        watchPanel.setLayout(new GridLayout(1,4));

        spilSeriePanel.setLayout(new GridLayout(2,1));
        spilSeriePanel.setBackground(new Color(32,32,32));
        playMediePanel.setLayout(new GridLayout(1,3));
        playMediePanel.setBackground(new Color(32,32,32));

        //Sætter hvert panel ind i deres eget scroll pane, så man kan scrolle
        forsideScroll = new JScrollPane(forsidePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        serierScroll = new JScrollPane(seriePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        filmScroll = new JScrollPane(filmPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        minListeScroll = new JScrollPane(minListePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        searchScroll = new JScrollPane(searchPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        watchScroll = new JScrollPane(watchPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        filterScroll = new JScrollPane(filterPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        playMedieScroll = new JScrollPane(playMediePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    public void navButtons() {
        try {
            BufferedImage image = ImageIO.read(new File("src/other/title.png"));
            logoBtn = new JButton(new ImageIcon(image.getScaledInstance(200,100,Image.SCALE_SMOOTH)));
            arrButtons.set(0,logoBtn); //Putter logoBtn til at være den første i array
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (JButton button: arrButtons) {
            // Her definerer vi knappernes dimensioner og egenskaber. Det er kun x værdien der ændres for hver knap
            // Focusable gør knapperne pæne (hvis focusable er true, vil der være en grim firkant rundt om teksten)
            button.setFocusable(false); //Sætter button til at være pænt ud
            button.setBackground(Color.black); //Gør baggrund på knappen sort
            button.setForeground(Color.white); //Gør teksten på knappen hvid
            button.setOpaque(true); //Sæt den her til true, for at for et farvet baggrund
            button.setBorderPainted(false); //Sætter den til at være false for at lave baggrunden
//           "Arial" is obviously the name of the font being used.
//           Font.PLAIN means plain text (as opposed to bold or italic).
//           40 is the font size (using the same numbering system for font size as Microsoft Word)
            button.setFont(new Font("Arial", Font.PLAIN, 25));

            //Tilføjer en button mouse listerne
            button.addMouseListener(new java.awt.event.MouseAdapter() {
            //Når musen peger på en nav knap, så ændre baggrunden sig til lidt mørkere
                public void mouseEntered(java.awt.event.MouseEvent evt){
                    button.setBackground(new Color(25,25,25));
                }
//                Når musen går væk fra en nav knap, så skifter den tilbage til den forrige baggrund
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(0,0,0));
                }
            });
            // Her tilføjer vi knapperne til vores nav panel
            navPanel.add(button);
        }

//        Sætter søge felt størrelse til 10 og tilføjer den til vores nav panel
        searchField.setColumns(10);
        navPanel.add(searchField);

        navPanel.add(filterGenre);
    }
    public void ActionListener() {
        //Kører hver gang der ændres noget på søgefeltet
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) { //Hver gang brugeren tilføjer et tegn kører den
                display(searchScroll);
                searchPanel.removeAll();
                search(arrAlle,arrBtnAlle, searchPanel);            }
            @Override
            public void removeUpdate(DocumentEvent de) { //Hver gang brugeren fjerner et tegn kører den
                display(searchScroll);
                searchPanel.removeAll();
                search(arrAlle,arrBtnAlle, searchPanel);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        /* Der er hernede vi tilføjer funktionalitet til hver af vores knapper
           Den fremviser den pågældende panel og sætter visibility af de andre paneler til false */
        //Se display metoden
        logoBtn.addActionListener(e -> {
            filterGenre.setSelectedIndex(0);
            display(forsideScroll);
        });
        forsideBtn.addActionListener(e -> {
            filterGenre.setSelectedIndex(0);
            display(forsideScroll);
        });
        serierBtn.addActionListener(e -> {
            filterGenre.setSelectedIndex(0);
            display(serierScroll);
        });
        filmBtn.addActionListener(e -> {
            filterGenre.setSelectedIndex(0);
            display(filmScroll);
        });
        filterGenre.addActionListener(e -> {
            filter(arrAlle,arrBtnAlle,filterPanel);
        });
        minListeBtn.addActionListener(e -> {
            filterGenre.setSelectedIndex(0);
            display(minListeScroll);
            minListeFunktion(arrAlle,arrBtnAlle,minListePanel);
        });

        }

    public void frameMethods() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Lukke programmet ordenligt
        frame.setSize(1500,700); //Størrelsen på frame
//        frame.setLayout(null);
        mainPanel.setBackground(new Color(217, 16, 16));
        mainPanel.add(navPanel, BorderLayout.NORTH); //Sætter navpanel i toppen af main panel

        mainPanel.add(forsideScroll, BorderLayout.CENTER); //Når man åbner programmet så er forsidepanel den der bliver vist
        activePanel = forsideScroll; //Aktive panel er forside i starten, kan ændres med display metoden

        frame.getContentPane().add(mainPanel); //Sætter main panel i frame
        mainPanel.setVisible(true); //Gør det synligt
        frame.setVisible(true); //Gør det synligt

    }

    public void display(JScrollPane currentPanel) {

        activePanel.setVisible(false); //Sætter den aktive panel til at være usynligt
        mainPanel.remove(activePanel); //Fjerner den atkive panel fra main panel
        mainPanel.add(currentPanel, BorderLayout.CENTER); //Tilføjer de currentPanel til main panel og sætter den i center position
        currentPanel.setVisible(true); //Sætter current panel til at være synligt
        mainPanel.revalidate(); //Opdatere main panel til at
        activePanel = currentPanel; //Sætter current panel til at være den aktive panel
    }

    //Den her metode søger for at hvis man trykker på et medie, så kan man se dens infp
    public void showMedia(JButton btn, String title, int ID, String aarstal, String rating, String genre,JButton btnWatch, boolean minListe, JButton tilfoejBtn, JButton fjernBtn, HashMap<String,Integer> saeson_episode) {

        //Vi har 2 JButton, den ene er fra selve de andre paneler, hvor den anden er en hel kopi (btnWatch)
        //Vi skal have 2 JButton, der en JButton kun kan være på et sted, så når vi trykker på
        //et medie, så vil den ikke blive fjernet fra panelet, der vi indsætter en kopi af den i stedet
        //til at blive vist i watch panel.

        //Sætter info a mediet som et array, så vi kan lave et for each loop
        ArrayList<String> arrInfo = new ArrayList<>(Arrays.asList("Titel: " +title,"Årstal: " +aarstal,"Rating: " +rating+"/10"));
        Favoritliste favor = new Favoritliste();
        JButton afspil = new JButton("Afspil");
        btn.addActionListener(e -> {
            watchPanel.removeAll();
            infoPanel.removeAll();
            display(watchScroll);
            infoPanel.setLayout( new GridLayout(5,1));
            infoPanel.setBackground(new Color(32,32,32));
            btnWatch.setBackground(new Color(32,32,32));
            watchPanel.add(btnWatch); //Indsætter kopi af medie knappen
            for (String info : arrInfo) { //Laver et for each loop, og sætter hvert info i info panel
                JLabel infoLabel = new JLabel(info);
                infoLabel.setForeground(Color.WHITE);
                infoLabel.setFont(new Font("Serif", Font.PLAIN, 28));
                infoPanel.add(infoLabel);
            }
            if(!minListe) {
                infoPanel.add(tilfoejBtn);
            } else {
                infoPanel.add(fjernBtn);
            }
            watchPanel.add(infoPanel);

            if(ID > 99) playSerie(saeson_episode); // Hvis en serie så tilføjer den en Combobox
            watchPanel.add(afspil);

        });

        tilfoejBtn.addActionListener(e -> {
            tilfoejBtn.setVisible(false);
            infoPanel.remove(tilfoejBtn);
            fjernBtn.setVisible(true);
            infoPanel.add(fjernBtn);
            infoPanel.revalidate();
            favor.tilføj_medie(title,ID);

        });

        fjernBtn.addActionListener(e ->{
            fjernBtn.setVisible(false);
            infoPanel.remove(fjernBtn);
            tilfoejBtn.setVisible(true);
            infoPanel.add(tilfoejBtn);
            infoPanel.revalidate();
            favor.fjern_medie(title, ID);
        });

        afspil.addActionListener(e -> {
             display(playMedieScroll);
             playMediePanel.removeAll();

             JLabel medieTxt;
             String medieInfo = "";
             if(ID > 99) {
                 medieInfo = seasonComboBox.getSelectedItem() + " " + episodeComboBox.getSelectedItem();
             }
             else  {
                 medieInfo = "";
             }

             medieTxt = new JLabel("Afspiller nu " + title + " " + medieInfo);
             JButton btnBlank = new JButton();
            JButton btnBlank2 = new JButton();

            btnBlank.setVisible(false);
            btnBlank2.setVisible(false);

            medieTxt.setForeground(Color.WHITE);
            medieTxt.setFont(new Font("Serif", Font.PLAIN, 28));
            playMediePanel.add(btnBlank);
            playMediePanel.add(medieTxt);
            playMediePanel.add(btnBlank2);
        });

    }
    public void playSerie(HashMap<String,Integer> saeson_episode) {
        spilSeriePanel.removeAll();
        ArrayList<String> arr1 = new ArrayList<>(saeson_episode.keySet());
        String[] arrSaeson = arr1.toArray(new String[0]); //Arraylist til array der JCombox kun tager en array og ik andet

        seasonComboBox = new JComboBox(arrSaeson);
        spilSeriePanel.add(seasonComboBox);
        watchPanel.add(spilSeriePanel);

        String[] epSaseon1 = new String[saeson_episode.get("Sæson 1")];
        for(int i = 0; i < epSaseon1.length; i++ ){
            epSaseon1[i] = "Episode " + (i +1);
        }
        episodeComboBox = new JComboBox(epSaseon1);
        spilSeriePanel.add(episodeComboBox);

        seasonComboBox.addActionListener(e -> {
            spilSeriePanel.remove(episodeComboBox);
            String[] epArr = new String[saeson_episode.get(seasonComboBox.getSelectedItem())];
            for(int i = 0; i < epArr.length; i++ ){
            epArr[i] = "Episode " + (i +1);
            }
            episodeComboBox = new JComboBox(epArr);
            spilSeriePanel.add(episodeComboBox);
            spilSeriePanel.revalidate();
        });
    }

    public void afspilMedie(){

    }
    public void readFile(ArrayList<Medie> arrMedie, ArrayList<JButton> arrBtn, JPanel panel){
        for (Medie m : arrMedie) {
        readImage(m, arrMedie, arrBtn, panel);
        }
    }
    public void search(ArrayList<Medie> arrMedie, ArrayList<JButton> arrBtn, JPanel panel) {
        display(searchScroll);
//        Sætter arrayliste til at være tomt, så medier ikke kan duplikieres hvis man søger igen
        arrBtn.clear();
        int totalMedie = 0; //Det skal bruges til at tælle hvor mange medier bliver vist frem til søgning
        for (Medie m : arrMedie) {
            //Viser kun film og serier titel til contains søge felt
            if ( Objects.equals(searchField.getText(), "")){ //Hvis søge felt er tomt, så viser det bare forsiden
                display(forsideScroll);
            }
            else if (m.titel.toLowerCase().contains(searchField.getText().toLowerCase()) ) { //Hvis en eller flere medie titler passer til søge feltet
                readImage(m,arrMedie,arrBtnSerier,panel);
                totalMedie++;
            }
        }
        if(totalMedie == 0) { //Hvis ens søgning ikke passer til nogen titler, så display den det her
            searchPanel.setLayout(new FlowLayout());
            JLabel txt = new JLabel("Kunne ikke finde noget fra din søgning");
            txt.setBounds(0,0,200,25);
            txt.setBackground(new Color (32,32,32));
            txt.setForeground(Color.white);
            txt.setOpaque(true);
            txt.setFont(new Font("Sans", Font.PLAIN, 45));
            searchPanel.add(txt);
        }
        else if (totalMedie%5 != 0 || totalMedie == 5) { //Hvis antal medie kan ikke divides med 5, eller antal er 5
            for(int i = 0; i< 5-(totalMedie%5); i++){ //Kører til at totalMedie mod 5 = 0
                JButton blankBtn = new JButton(); //lave en knap der har samme baggrund som baggrund, så man ikke kan se den
                blankBtn.setBackground(new Color(32,32,32));
                blankBtn.setOpaque(true);
                blankBtn.setBorderPainted(false);
                searchPanel.add(blankBtn);
            }
            searchPanel.setLayout(new GridLayout(totalMedie / 5 + 1, 5)); //Laver antal grid der passer til antal totalmedie og de usynlig knapper
        }
        else { //Ellers hvis total medie går op i 5 og ikk er 5, fx 20, så sætte rækker til at være 4 (totalMedie som er 20/5 = 4)
            searchPanel.setLayout(new GridLayout(totalMedie/5, 5));
        }
        searchPanel.revalidate(); //Opdatere panelet 

    }
    public void filter(ArrayList<Medie> arrMedie, ArrayList<JButton> arrBtn, JPanel panel) {
        display(filterScroll);
        filterPanel.removeAll();
        int totalMedie = 0; //Det skal bruges til at tælle hvor mange medier bliver vist frem til søgning
        for (Medie m : arrMedie) {
            if (filterGenre.getSelectedItem().equals(m.genre)) {
                readImage(m,arrMedie,arrBtnSerier,panel);
            if (m.genre.contains(filterGenre.getSelectedItem().toString())) {
                JButton tilfoejBtn = new JButton("Tilføj til min liste");
                JButton fjernBtn = new JButton("Fjern fra min liste");
                try { //samme kode fra readFile
                    BufferedImage image = ImageIO.read(new File("src/forsider/"+m.titel+".jpg"));
                    JButton picBtn = new JButton(new ImageIcon(image.getScaledInstance(250,350,Image.SCALE_SMOOTH)));
                    picBtn.setBackground(new Color(32,32,32));
                    picBtn.setFocusable(false);
                    picBtn.setBackground(Color.black);
                    picBtn.setOpaque(true);
                    picBtn.setBorderPainted(false); //Sæt den her til true, for at for et farvet baggrund
                    arrBtn.add(picBtn);
                    panel.add(picBtn);
                    showMedia(picBtn, m.titel, m.aarstal, m.rating,m.genre,picBtn, m.minListe, tilfoejBtn,fjernBtn); //Her laver vi ikke en kopi af picbtn, der det ikke er relevant
                    totalMedie++;
                } catch (FileNotFoundException fnfe) {
                    System.out.println("Der fandtes ingen resultater for " + m.titel);
                } catch (Exception e) {
                    System.out.println("Noget gik galt");
                }
            }
        }
        if(totalMedie == 0) { //Hvis ens søgning ikke passer til nogen titler, så display den det her
            display(forsideScroll);
        }
        else if (totalMedie%5 != 0 || totalMedie == 5) { //Hvis antal medie kan ikke divides med 5, eller antal er 5
            for(int i = 0; i< 5-(totalMedie%5); i++){ //Kører til at totalMedie mod 5 = 0
                JButton blankBtn = new JButton(); //lave en knap der har samme baggrund som baggrund, så man ikke kan se den
                blankBtn.setBackground(new Color(32,32,32));
                blankBtn.setOpaque(true);
                blankBtn.setBorderPainted(false);
                filterPanel.add(blankBtn);
            }
            filterPanel.setLayout(new GridLayout(totalMedie / 5 + 1, 5)); //Laver antal grid der passer til antal totalmedie og de usynlig knapper
        }
        else { //Ellers hvis total medie går op i 5 og ikk er 5, fx 20, så sætte rækker til at være 4 (totalMedie som er 20/5 = 4)
            filterPanel.setLayout(new GridLayout(totalMedie/5, 5));
        }
        filterPanel.revalidate(); //Opdatere panelet
    }

    public void minListeFunktion(ArrayList<Medie> arrMedie, ArrayList<JButton> arrBtn, JPanel panel) {
        int totalMedie = 0; //Det skal bruges til at tælle hvor mange medier bliver vist frem til søgning
        Favoritliste favor = new Favoritliste();
        minListePanel.removeAll();
        for (Medie m : arrMedie) {
            if (favor.indlaes_medier().contains(m.titel + " " + m.ID)) {
                m.minListe = true;  //Alle medierne skal have minliste til at være true, siden de er inden i txt filen
                readImage(m, arrMedie, arrBtn, panel);
                totalMedie++;
            }
        }
        if(totalMedie == 0) { //Hvis ens søgning ikke passer til nogen titler, så display den det her
            minListePanel.setLayout(new FlowLayout());
            JLabel txt = new JLabel("Din liste er tom");
            txt.setBounds(0,0,200,25);
            txt.setBackground(new Color (32,32,32));
            txt.setForeground(Color.white);
            txt.setOpaque(true);
            txt.setFont(new Font("Sans", Font.PLAIN, 45));
            minListePanel.add(txt);
        }
        else if (totalMedie%5 != 0 || totalMedie == 5) { //Hvis antal medie kan ikke divides med 5, eller antal er 5
            for(int i = 0; i< 5-(totalMedie%5); i++){ //Kører til at totalMedie mod 5 = 0
                JButton blankBtn = new JButton(); //lave en knap der har samme baggrund som baggrund, så man ikke kan se den
                blankBtn.setBackground(new Color(32,32,32));
                blankBtn.setOpaque(true);
                blankBtn.setBorderPainted(false);
                minListePanel.add(blankBtn);
            }
            minListePanel.setLayout(new GridLayout(totalMedie / 5 + 1, 5)); //Laver antal grid der passer til antal totalmedie og de usynlig knapper
        }
        else { //Ellers hvis total medie går op i 5 og ikk er 5, fx 20, så sætte rækker til at være 4 (totalMedie som er 20/5 = 4)
            minListePanel.setLayout(new GridLayout(totalMedie/5, 5));
        }
        minListePanel.revalidate(); //Opdatere panelet
    }

    public void readImage(Medie m, ArrayList<Medie> arrMedie, ArrayList<JButton> arrBtn, JPanel panel) {
        JButton tilfoejBtn = new JButton("Tilføj til min liste");
        JButton fjernBtn = new JButton("Fjern fra min liste");
        try { //samme kode fra readFile
            BufferedImage image = null;
            if (m.ID < 100) {
                image = ImageIO.read(new File("src/filmplakater/"+m.titel+".jpg"));
            } else {
                image = ImageIO.read(new File("src/serieforsider/"+m.titel+".jpg"));
            }
            JButton picBtn = new JButton(new ImageIcon(image.getScaledInstance(250,350,Image.SCALE_SMOOTH)));
            picBtn.setBackground(new Color(32,32,32));
            picBtn.setFocusable(false);
            picBtn.setBackground(Color.black);
            picBtn.setOpaque(true);
            picBtn.setBorderPainted(false); //Sæt den her til true, for at for et farvet baggrund
            arrBtn.add(picBtn);
            panel.add(picBtn);
            showMedia(picBtn, m.titel, m.ID,m.aarstal, m.rating,m.genre,picBtn, m.minListe, tilfoejBtn,fjernBtn, m.getSaeson_episode()); //Her laver vi ikke en kopi af picbtn, der det ikke er relevant

        } catch (FileNotFoundException fnfe) {
            System.out.println("Der fandtes ingen resultater for " + m.titel);
         } catch (IOException ioe){
            System.out.println("Kunne ikke indlæse input fil: " + m.titel);
        }
        catch (NullPointerException npe) {
            System.out.println("Null pointer med " + m.titel + " "+ npe.getMessage());
        } catch (Exception e) {
            System.out.println("Noget gik galt med " + m.titel+ " " +e.getMessage());
        }

    }
}
