package proyectoEI1048Vista;

/**
 * Created by Carlos on 18/11/2014.
 */
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.text.Document;

import org.jdom.JDOMException;

import proyectoEI1048Controlador.Controlador;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


@SuppressWarnings("restriction")
public class MainApplication extends Application {
	private Stage stage;
	//Fuente fuente;
	private Controlador controlador;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException, JDOMException {
		this.controlador = new Controlador();
		stage= primaryStage;
		primaryStage.setTitle("Lector RSS-");
		primaryStage.setScene(mainScene());
		primaryStage.show();
	}

    /*------------------------------------------------------------------

    Carpetas

     ------------------------------------------------------------------------*/

    public Scene mainScene(){
		BorderPane border = new BorderPane();
		HBox hbox= new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER);
		hbox.setStyle("-fx-background-color: #336699;");
		Text title = new Text("Lector de Rss-Fuentes");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		title.setFill(Color.WHITE);
		hbox.getChildren().add(title);
		border.setTop(hbox);



		final ListView<String> list = new ListView<String>();
		ObservableList<String> items = FXCollections.observableArrayList();

		final Map<String,Map<String,String>> carpetas = controlador.getCarpetas();


        Iterator<String> iter= carpetas.keySet().iterator();

		while(iter.hasNext()){
			items.add(iter.next());
		}
		Collections.sort(items);
		list.setItems(items);

		VBox vbox= new VBox();
		vbox.setSpacing(8);

        HBox hbox2= new HBox();
        hbox2.setPadding(new Insets(15, 12, 15, 12));
        hbox2.setSpacing(10);
        Label label1 = new Label("Busqueda:");
        final TextField textField = new TextField ();

        final Label labelError = new Label("Selecciona una carpeta para borrarla o Acceder a ella.");
        Button btnBusq = new Button();
        btnBusq.setText("Buscar");
        btnBusq.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (textField.getText() == null || textField.getText().trim().isEmpty()) {
                    labelError.setText("No has escrito nada");
                    labelError.setTextFill(Color.RED);
                } else {
                    String nombre = textField.getText();
                    HashMap<String,String> fuentes =(HashMap<String,String>) controlador.buscaEtiqueta(nombre);

                    stage.setScene(addSceneFuentesBusqueda(fuentes));
                }
            }
        });
        hbox2.setAlignment(Pos.CENTER_RIGHT);
        hbox2.getChildren().addAll(label1,textField,btnBusq);



        vbox.getChildren().addAll(hbox2,list);
		vbox.setAlignment(Pos.CENTER);



		VBox vBox2= new VBox();
		vBox2.setAlignment(Pos.CENTER);




		Button btn = new Button();
		btn.setText("Añadir carpeta");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.setScene(addSceneAnyadir());
			}
		});
		Button btn2 = new Button();
		btn2.setText("Borrar carpeta");
		btn2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
                int numero = 0;
				if (list.getSelectionModel().getSelectedItem() == null) {
					labelError.setText("No has seleccionado ninguna Fuente");
					labelError.setTextFill(Color.RED);
				} else {
					String nombre = list.getSelectionModel().getSelectedItem();
                    Boolean existe=false;
                    try {
						existe = controlador.removeCapeta(nombre);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    if(!existe){
                        labelError.setText("No puedes borrar una carpeta llena");
                        labelError.setTextFill(Color.RED);
                    }else
					    stage.setScene(mainScene());
				}
			}
		});

		Button btn3 = new Button();
		btn3.setText("Mostrar");
		btn3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (list.getSelectionModel().getSelectedItem() == null) {
					labelError.setText("No has seleccionado ninguna Carpeta");
					labelError.setTextFill(Color.RED);
				} else {
                    String nombre = list.getSelectionModel().getSelectedItem();
                    HashMap<String,String> fuentes =(HashMap<String,String>) carpetas.get(nombre);
					stage.setScene(addSceneFuentes(fuentes,nombre));
				}
			}
		});

		vBox2.setPadding(new Insets(5, 12, 5, 12));
		HBox hb3 = new HBox();
		hb3.setPadding(new Insets(5, 12, 5, 12));
		hb3.getChildren().add(btn);
		hb3.getChildren().add(btn2);
		hb3.getChildren().add(btn3);
		hb3.setSpacing(10);
		hb3.setAlignment(Pos.CENTER);
		vBox2.getChildren().addAll(labelError,hb3);
		border.setBottom(vBox2);

		border.setCenter(vbox);
		return new Scene(border, 950, 700);

	}

	public Scene addSceneAnyadir(){

		BorderPane border = new BorderPane();
		HBox hbox= new HBox();
		hbox.setPadding(new Insets(15, 12, 15,12));
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER);
		hbox.setStyle("-fx-background-color: #336699;");
		Text title = new Text("Lector de Rss - añadir Carpeta");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		title.setFill(Color.WHITE);
		hbox.getChildren().add(title);
		border.setTop(hbox);

		VBox vbox= new VBox();
		vbox.setSpacing(8);

		Label label1 = new Label("Nombre:");
		final TextField textField = new TextField ();

		HBox hb1 = new HBox();
		hb1.getChildren().addAll(label1, textField);
		hb1.setSpacing(10);
		hb1.setAlignment(Pos.CENTER_LEFT);

		final Label label3 = new Label("");
		vbox.getChildren().addAll(hb1,label3);
		vbox.setAlignment(Pos.CENTER);

		border.setCenter(vbox);



		Button btn = new Button();
		btn.setText("Añadir");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String nombre = textField.getText();
				//fuente.setParams(textField.getText(),textField2.getText());
				if(controlador.existsCarpeta(nombre)){
					label3.setText("La Carpeta que estás intentando añadir ya existe");
					label3.setTextFill(Color.RED);
				}else {
                    try {
                        controlador.anyadirCarpeta(nombre);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setScene(mainScene());
				}
			}
		});
		Button btn2 = new Button();
		btn2.setText("Cancelar");
		btn2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.setScene(mainScene());
			}
		});


		HBox hb3 = new HBox();
		hb3.getChildren().addAll(btn, btn2);
		hb3.setPadding(new Insets(0, 12, 20, 12));
		hb3.setSpacing(10);
		hb3.setAlignment(Pos.CENTER);
		border.setBottom(hb3);

		return new Scene(border, 950, 700);
	}

    /*------------------------------------------------------------------

    Fuentes

     ------------------------------------------------------------------------*/

    public Scene addSceneAnyadirFuente(String nCarpeta){

        BorderPane border = new BorderPane();
        HBox hbox= new HBox();
        hbox.setPadding(new Insets(15, 12, 15,12));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: #336699;");
        Text title = new Text("Lector de Rss - añadir Carpeta");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        title.setFill(Color.WHITE);
        hbox.getChildren().add(title);
        border.setTop(hbox);

        VBox vbox= new VBox();
        vbox.setSpacing(8);

        Label label1 = new Label("Nombre:");
        final TextField textField = new TextField ();
        Label label2 = new Label("URL:");
        final TextField textField2 = new TextField ();

        HBox hb1 = new HBox();
        hb1.getChildren().addAll(label1, textField);
        hb1.setSpacing(10);
        hb1.setAlignment(Pos.CENTER_LEFT);

        HBox hb2 = new HBox();
        hb2.getChildren().addAll(label2, textField2);
        hb2.setSpacing(10);
        hb2.setAlignment(Pos.CENTER_LEFT);


        final Label label3 = new Label("");
        vbox.getChildren().addAll(hb1,hb2,label3);
        vbox.setAlignment(Pos.CENTER);

        border.setCenter(vbox);



        Button btn = new Button();
        btn.setText("Añadir");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nombre = textField.getText();
                String url = textField2.getText();
                //fuente.setParams(textField.getText(),textField2.getText());
                if(controlador.existsFuente(nombre, url)){
                    label3.setText("La fuente que estás intentando añadir ya existe");
                    label3.setTextFill(Color.RED);
                }else {

                    try {
                        controlador.anyadirFuente(nombre, url, nCarpeta);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setScene(mainScene());
                }
            }
        });
        Button btn2 = new Button();
        btn2.setText("Cancelar");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(mainScene());
            }
        });


        HBox hb3 = new HBox();
        hb3.getChildren().addAll(btn, btn2);
        hb3.setPadding(new Insets(0, 12, 20, 12));
        hb3.setSpacing(10);
        hb3.setAlignment(Pos.CENTER);
        border.setBottom(hb3);

        return new Scene(border, 950, 700);
    }

    public Scene addSceneMostrar(List<String> info) throws IllegalArgumentException, IOException, FeedException{

        BorderPane border = new BorderPane();
        HBox hbox= new HBox();
        hbox.setPadding(new Insets(15, 12, 15,12));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: #336699;");
        final Text title = new Text("Lector de Rss - Entradas");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        title.setFill(Color.WHITE);
        hbox.getChildren().add(title);
        border.setTop(hbox);

        //Prueba
        String prueba = "";
        for (int j = 0; j< info.size();j++){
            prueba = prueba + info.get(j);
        }
        //System.out.println(prueba);
        String[]prueba1 = prueba.split("45802276");


        final ListView<HBox> list = new ListView<HBox>();
        ObservableList<HBox> items = FXCollections.observableArrayList();

        HashMap<String,String> map = new HashMap<String,String>();
        for(int i=0;i<prueba1.length;i++){

            String[] prueba2 = prueba1[i].split("53727847");
            String[] prueba3= prueba2[0].split("\n");
            HBox hboxlista = new HBox();
            Label label = new Label();
            String nombre = prueba3[0]+"\n"+prueba3[1];
            label.setText(nombre);
            Image img;
            if(controlador.fuenteLeida(nombre)){
                img =  new Image(getClass().getClassLoader().getResourceAsStream("open.png"));
            }else{
                img =  new Image(getClass().getClassLoader().getResourceAsStream("closed.png"));
            }

            Button button = new Button("",new ImageView(img));
            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);
            button.setStyle("-fx-background-color: transparent;");
            hboxlista.getChildren().addAll(label, button);
            items.add(hboxlista);
            //items.add(prueba3[0]+"\n"+prueba3[1]);
            map.put(prueba3[0] + "\n" + prueba3[1], prueba2[1]);
        }
        list.setItems(items);




        VBox vbox= new VBox();
        vbox.setSpacing(8);

        vbox.getChildren().add(list);
        vbox.setAlignment(Pos.CENTER);
        border.setCenter(vbox);


		/*
        String articulo = list.getSelectionModel().getSelectedItem();
        String[] art = articulo.split("\n");
        System.out.println(art[0]+art[1]);
		 */




        Button btn2 = new Button();
        btn2.setText("Volver");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(mainScene());
            }
        });

        Button btn3 = new Button();
        btn3.setText("Mostrar");
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*String articulo = list.getSelectionModel().getSelectedItem();
                System.out.println(articulo);
                String[] art = articulo.split("\n");
                //title.setText(art[1]);
                try {
                    stage.setScene(addSceneWebView(map.get(articulo), art[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
                HBox hb =  list.getSelectionModel().getSelectedItem();
                ObservableList observable = hb.getChildren();
                Label lab = (Label) observable.get(0);
                String articulo = lab.getText();
                String[] art = articulo.split("\n");

                //title.setText(art[1]);
                try {
                    if(!controlador.fuenteLeida(articulo))
                        controlador.addLeido(articulo);
                    stage.setScene(addSceneWebView(map.get(articulo), art[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        HBox hb3 = new HBox();
        hb3.getChildren().addAll(btn2,btn3);
        hb3.setPadding(new Insets(0, 12, 20, 12));
        hb3.setSpacing(10);
        hb3.setAlignment(Pos.CENTER);
        border.setBottom(hb3);

        return new Scene(border, 950, 700);
    }

    public Scene addSceneWebView(String url, String descr) throws IOException {

        BorderPane border = new BorderPane();
        HBox hbox= new HBox();
        hbox.setPadding(new Insets(15, 12, 15,12));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: #336699;");
        Text title = new Text(descr);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        title.setFill(Color.WHITE);
        hbox.getChildren().add(title);
        border.setTop(hbox);




		/*
        VBox vbox= new VBox();
        vbox.setSpacing(8);
        vbox.getChildren().add(prueba);
        vbox.setAlignment(Pos.CENTER);
        border.setCenter(vbox);
		 */

        WebView browser = new WebView();

        WebEngine webEngine = browser.getEngine();
        webEngine.loadContent(url);
        //webEngine.load(url);

        VBox vbox= new VBox();
        vbox.setSpacing(8);
        vbox.getChildren().add(browser);
        vbox.setAlignment(Pos.CENTER);
        border.setCenter(vbox);

		/*
        String articulo = list.getSelectionModel().getSelectedItem();
        String[] art = articulo.split("\n");
        System.out.println(art[0]+art[1]);
		 */




        Button btn2 = new Button();
        btn2.setText("Volver");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(mainScene());
            }
        });



        HBox hb3 = new HBox();
        hb3.getChildren().addAll(btn2);
        hb3.setPadding(new Insets(0, 12, 20, 12));
        hb3.setSpacing(10);
        hb3.setAlignment(Pos.CENTER);
        border.setBottom(hb3);

        return new Scene(border, 950, 700);
    }

    public Scene addSceneFuentes(HashMap<String,String> fuentes, String nCarpeta){

        BorderPane border = new BorderPane();
        HBox hbox= new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: #336699;");
        Text title = new Text("Lector de Rss-Fuentes");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        title.setFill(Color.WHITE);
        hbox.getChildren().add(title);
        border.setTop(hbox);



        final ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();

        Iterator<String> iter = fuentes.keySet().iterator();

        while(iter.hasNext()){
            //System.out.println((String) iter.next());
            items.add(iter.next());
        }
        Collections.sort(items);
        list.setItems(items);

        VBox vbox= new VBox();
        vbox.setSpacing(8);

        vbox.getChildren().add(list);
        vbox.setAlignment(Pos.CENTER);



        VBox vBox2= new VBox();
        vBox2.setAlignment(Pos.CENTER);

        final Label labelError = new Label("Selecciona una fuente para borrarla o mostrarla.");



        Button btn = new Button();
        btn.setText("Añadir fuente");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(addSceneAnyadirFuente(nCarpeta));
            }
        });
        Button btn2 = new Button();
        btn2.setText("Borrar fuente");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (list.getSelectionModel().getSelectedItem() == null) {
                    labelError.setText("No has seleccionado ninguna Fuente");
                    labelError.setTextFill(Color.RED);
                } else {
                    String nombre = list.getSelectionModel().getSelectedItem();
                    String url = fuentes.get(nombre);
                    try {
                        controlador.removeFuente(nombre, url, nCarpeta);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.setScene(mainScene());
                }
            }
        });

        Button btn3 = new Button();
        btn3.setText("Mostrar");
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (list.getSelectionModel().getSelectedItem() == null) {
                    labelError.setText("No has seleccionado ninguna Fuente");
                    labelError.setTextFill(Color.RED);
                } else {
                    String nombre = list.getSelectionModel().getSelectedItem();
                    String url = fuentes.get(nombre);
                    List<String> info = controlador.info(nombre, url);
                    //fuente.setParams(nombre, url);
                    try {
                        stage.setScene(addSceneMostrar(info));
                    } catch (FeedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        Button btn4 = new Button();
        btn4.setText("Volver");
        btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(mainScene());
            }
        });

        Button btn5 = new Button();
        btn5.setText("Ver etiquetas");
        btn5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (list.getSelectionModel().getSelectedItem() == null) {
                    labelError.setText("No has seleccionado ninguna Fuente");
                    labelError.setTextFill(Color.RED);
                } else {
                    String nombre = list.getSelectionModel().getSelectedItem();
                    stage.setScene(SceneEtiquetas(nCarpeta,nombre));
                }
            }
        });
        Button btn6 = new Button();
        btn6.setText("Cambiar fuente de carpeta");
        btn6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (list.getSelectionModel().getSelectedItem() == null) {
                    labelError.setText("No has seleccionado ninguna Fuente");
                    labelError.setTextFill(Color.RED);
                } else {
                    String nombre = list.getSelectionModel().getSelectedItem();
                    String url = fuentes.get(nombre);
                    stage.setScene(sceneCambiarCarpeta(nCarpeta,nombre,url));
                }
            }
        });


        vBox2.setPadding(new Insets(5, 12, 5, 12));
        HBox hb3 = new HBox();
        hb3.setPadding(new Insets(5, 12, 5, 12));
        hb3.getChildren().add(btn);
        hb3.getChildren().add(btn2);
        hb3.getChildren().add(btn5);
        hb3.getChildren().add(btn6);
        hb3.setSpacing(10);
        hb3.setAlignment(Pos.CENTER);
        HBox hb4 = new HBox();
        hb4.setPadding(new Insets(5, 12, 5, 12));
        hb4.getChildren().add(btn3);
        hb4.getChildren().add(btn4);
        hb4.setSpacing(10);
        hb4.setAlignment(Pos.CENTER);
        vBox2.getChildren().addAll(labelError,hb4,hb3);
        border.setBottom(vBox2);

        border.setCenter(vbox);
        return new Scene(border, 950, 700);

    }

    public Scene addSceneFuentesBusqueda(HashMap<String,String> fuentes){

        BorderPane border = new BorderPane();
        HBox hbox= new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: #336699;");
        Text title = new Text("Lector de Rss-Fuentes");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        title.setFill(Color.WHITE);
        hbox.getChildren().add(title);
        border.setTop(hbox);



        final ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();

        Iterator<String> iter = fuentes.keySet().iterator();

        while(iter.hasNext()){
            //System.out.println((String) iter.next());
            items.add(iter.next());
        }
        Collections.sort(items);
        list.setItems(items);

        VBox vbox= new VBox();
        vbox.setSpacing(8);

        vbox.getChildren().add(list);
        vbox.setAlignment(Pos.CENTER);



        VBox vBox2= new VBox();
        vBox2.setAlignment(Pos.CENTER);

        final Label labelError = new Label("Selecciona una fuente para borrarla o mostrarla.");


        Button btn3 = new Button();
        btn3.setText("Mostrar");
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (list.getSelectionModel().getSelectedItem() == null) {
                    labelError.setText("No has seleccionado ninguna Fuente");
                    labelError.setTextFill(Color.RED);
                } else {
                    String nombre = list.getSelectionModel().getSelectedItem();
                    String url = fuentes.get(nombre);
                    List<String> info = controlador.info(nombre, url);
                    //fuente.setParams(nombre, url);
                    try {
                        stage.setScene(addSceneMostrar(info));
                    } catch (FeedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        Button btn4 = new Button();
        btn4.setText("Volver");
        btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(mainScene());
            }
        });


        vBox2.setPadding(new Insets(5, 12, 5, 12));
        HBox hb4 = new HBox();
        hb4.setPadding(new Insets(5, 12, 5, 12));
        hb4.getChildren().add(btn3);
        hb4.getChildren().add(btn4);
        hb4.setSpacing(10);
        hb4.setAlignment(Pos.CENTER);
        vBox2.getChildren().addAll(labelError,hb4);
        border.setBottom(vBox2);

        border.setCenter(vbox);
        return new Scene(border, 950, 700);

    }

    public Scene sceneCambiarCarpeta(String nCarpeta, String nFuente,String nUrl){

        BorderPane border = new BorderPane();
        HBox hbox= new HBox();
        hbox.setPadding(new Insets(15, 12, 15,12));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: #336699;");
        Text title = new Text("Lector de Rss - añadir Carpeta");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        title.setFill(Color.WHITE);
        hbox.getChildren().add(title);
        border.setTop(hbox);

        VBox vbox= new VBox();
        vbox.setSpacing(8);

        Label label1 = new Label("Carpeta:");

        ObservableList<String> items = FXCollections.observableArrayList();
        final Map<String,Map<String,String>> carpetas = controlador.getCarpetas();


        Iterator<String> iter= carpetas.keySet().iterator();

        while(iter.hasNext()){
            items.add(iter.next());
        }
        items.remove(nCarpeta);
        Collections.sort(items);
        final ComboBox comboBox = new ComboBox (items);

        HBox hb1 = new HBox();
        hb1.getChildren().addAll(label1, comboBox);
        hb1.setSpacing(10);
        hb1.setAlignment(Pos.CENTER_LEFT);

        final Label label3 = new Label("");
        vbox.getChildren().addAll(hb1,label3);
        vbox.setAlignment(Pos.CENTER);

        border.setCenter(vbox);



        Button btn = new Button();
        btn.setText("Aceptar");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(comboBox.getValue() != null && !comboBox.getValue().toString().isEmpty()) {
                        controlador.cambiarFuenteCarpeta(nFuente, nCarpeta, comboBox.getValue().toString(), nUrl);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setScene(mainScene());
            }
        });
        Button btn2 = new Button();
        btn2.setText("Cancelar");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(mainScene());
            }
        });

        HBox hb3 = new HBox();
        hb3.getChildren().addAll(btn, btn2);
        hb3.setPadding(new Insets(0, 12, 20, 12));
        hb3.setSpacing(10);
        hb3.setAlignment(Pos.CENTER);
        border.setBottom(hb3);

        return new Scene(border, 950, 700);


    }

    /*------------------------------------------------------------------

    Etiquetas

     ------------------------------------------------------------------------*/

    public Scene SceneEtiquetas(String nCarpeta, String nFuente){

        BorderPane border = new BorderPane();
        HBox hbox= new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: #336699;");
        Text title = new Text("Lector de Rss-Etiquetas");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        title.setFill(Color.WHITE);
        hbox.getChildren().add(title);
        border.setTop(hbox);



        final ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();
        Iterator<String> iter = controlador.getEtiquetas(nCarpeta,nFuente).iterator();

        while(iter.hasNext()){
            //System.out.println((String) iter.next());
            items.add(iter.next());
        }
        Collections.sort(items);
        list.setItems(items);

        VBox vbox= new VBox();
        vbox.setSpacing(8);

        vbox.getChildren().add(list);
        vbox.setAlignment(Pos.CENTER);



        VBox vBox2= new VBox();
        vBox2.setAlignment(Pos.CENTER);

        final Label labelError = new Label("Selecciona una fuente para borrarla o mostrarla.");



        Button btn = new Button();
        btn.setText("Añadir Etiqueta");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(sceneAddEtiquetas(nCarpeta,nFuente));
            }
        });
        Button btn2 = new Button();
        btn2.setText("Borrar Etiqueta");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (list.getSelectionModel().getSelectedItem() == null) {
                    labelError.setText("No has seleccionado ninguna etiqueta");
                    labelError.setTextFill(Color.RED);
                }else {
                    String nombre = list.getSelectionModel().getSelectedItem();
                    if (!controlador.existsEtiqueta(nCarpeta, nFuente, nombre)) {
                        labelError.setText("La Etiquetaque estás intentando no existe");
                        labelError.setTextFill(Color.RED);
                    } else {
                        controlador.deleteEtiqueta(nCarpeta, nFuente, nombre);
                        stage.setScene(mainScene());
                    }
                }
            }
        });
        Button btn4 = new Button();
        btn4.setText("Volver");
        btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(mainScene());
            }
        });

        vBox2.setPadding(new Insets(5, 12, 5, 12));
        HBox hb3 = new HBox();
        hb3.setPadding(new Insets(5, 12, 5, 12));
        hb3.getChildren().add(btn);
        hb3.getChildren().add(btn2);
        hb3.getChildren().add(btn4);
        hb3.setSpacing(10);
        hb3.setAlignment(Pos.CENTER);;
        vBox2.getChildren().addAll(labelError,hb3);
        border.setBottom(vBox2);

        border.setCenter(vbox);
        return new Scene(border, 950, 700);

    }

    public Scene sceneAddEtiquetas(String nCarpeta, String nFuente){

        BorderPane border = new BorderPane();
        HBox hbox= new HBox();
        hbox.setPadding(new Insets(15, 12, 15,12));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: #336699;");
        Text title = new Text("Lector de Rss - añadir Carpeta");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        title.setFill(Color.WHITE);
        hbox.getChildren().add(title);
        border.setTop(hbox);

        VBox vbox= new VBox();
        vbox.setSpacing(8);

        Label label1 = new Label("Nombre:");
        final TextField textField = new TextField ();

        HBox hb1 = new HBox();
        hb1.getChildren().addAll(label1, textField);
        hb1.setSpacing(10);
        hb1.setAlignment(Pos.CENTER_LEFT);

        final Label label3 = new Label("");
        vbox.getChildren().addAll(hb1,label3);
        vbox.setAlignment(Pos.CENTER);

        border.setCenter(vbox);



        Button btn = new Button();
        btn.setText("Añadir");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nombre = textField.getText();
                //fuente.setParams(textField.getText(),textField2.getText());
                if(controlador.existsEtiqueta(nCarpeta, nFuente, nombre)){
                    label3.setText("La Etiquetaque estás intentando añadir ya existe");
                    label3.setTextFill(Color.RED);
                }else {
                    controlador.addEtiqueta(nCarpeta, nFuente, nombre);
                    stage.setScene(mainScene());
                }
            }
        });
        Button btn2 = new Button();
        btn2.setText("Cancelar");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setScene(mainScene());
            }
        });


        HBox hb3 = new HBox();
        hb3.getChildren().addAll(btn, btn2);
        hb3.setPadding(new Insets(0, 12, 20, 12));
        hb3.setSpacing(10);
        hb3.setAlignment(Pos.CENTER);
        border.setBottom(hb3);

        return new Scene(border, 950, 700);


    }





}