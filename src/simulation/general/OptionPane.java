/****************************************************************************
 Creates a JFrame for user input.  Handles strings, booleans, and ints with the option of being timed.
 An instance of the OptionPane class needs to be created in order to call the functions.
 *****************************************************************************
 Function List:
 // Basic JPanes  (message=message to display in JPane, scriptName=name of script calling for JPane)
 optionPaneString(String message, String scriptName)
 optionPaneInt(String message, String scriptName)
 optionPaneBoolean(String message, String scriptName)
 // Timed JPanes (defaultReturn = value to return if timeout, timeMS = time to timeout)
 optionPaneStringTimed(String message, String scriptName,int defaultReturn, int timeMS)
 optionPaneIntTimed(String message, String scriptName,int defaultReturn, int timeMS)
 optionPaneBooleanTimed(String message, String scriptName,boolean defaultReturn, int timeMS)
 isOptionPaneOpen()  (Returns true if option pane is open, false otherwise)
 *****************************************************************************
 Typical construction:
 OptionPane optionPaneName = new OptionPane(); //Creates instance of OptionPane
 String inputString = optionPaneName.optionPaneString("Please input a string", "SampleScript");  //Opens a JPane
 while(optionPaneName.isOptionPaneOpen()){ sleep(100); }  //Pauses script until option pane is closed
 *****************************************************************************/

package simulation.general;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static simulation.general.General.println;

public class OptionPane {
    private boolean optionPaneOpen; //True if script is busy handling option pane
    private Timer timer;
    private JFrame frame = new JFrame();

    /**
     * Creates an option pane that requests a String from the user
     * @param message Prompt for the input (ie. "What is your name?")
     * @param scriptName Name of the script
     */
    public String optionPaneString(String message,String scriptName){
        optionPaneOpen = true;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String input = JOptionPane.showInputDialog(frame,message,scriptName,0);

        if(frame.isActive())
            frame.dispose();
        optionPaneOpen =false;
        return input;
    }

    /**
     * Creates an option pane that requests an int from the user
     * @param message Prompt for the input (ie. "How old are you?")
     * @param scriptName Name of the script
     */
    public int optionPaneInt(String message, String scriptName){
        optionPaneOpen = true;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String input = JOptionPane.showInputDialog(frame,message,scriptName,0);
        int inputInt = Integer.parseInt(input);

        if(frame.isActive())
            frame.dispose();
        optionPaneOpen = false;
        return inputInt;
    }

    /**
     * Creates an option pane that requests a Yes/No from the user
     * @param message Prompt for the input (ie. "Do you like pie?")
     * @param scriptName Name of the script
     */
    public boolean optionPaneBoolean(String message, String scriptName){
        optionPaneOpen = true;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int n = JOptionPane.showConfirmDialog(
                frame,
                message,
                "ScriptsForMains: "+scriptName,
                JOptionPane.YES_NO_OPTION);

        if(frame.isActive())
            frame.dispose();
        optionPaneOpen = false;
        if (n==0)
            return true;
        else
            return false;
    }

    /**
     * Creates an option pane that requests a String from the user. After set time returns the default option.
     * @param message Prompt for the input (ie. "What is your name?")
     * @param scriptName Name of the script
     * @param defaultReturn Default value to return at specified time
     * @param timeMS Time(ms) before returning default value
     */
    public String optionPaneStringTimed(String message, String scriptName,String defaultReturn, int timeMS) {
        optionPaneOpen=true;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit.getDefaultToolkit().addAWTEventListener(
                new AWTEventListener() {
                    @Override
                    public void eventDispatched(AWTEvent event) {
                        Object source = event.getSource();
                        if (source instanceof Component) {
                            Component comp = (Component) source;
                            Window win = null;
                            if (comp instanceof Window) {
                                win = (Window) comp;
                            } else {
                                win = SwingUtilities.windowForComponent(comp);
                            }
                            if (win == frame) {
                                timer.restart();
                            }
                        }
                    }
                },
                AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK
                        | AWTEvent.MOUSE_MOTION_EVENT_MASK
                        | AWTEvent.MOUSE_WHEEL_EVENT_MASK);

        timer = new Timer(timeMS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        timer.start();
        String input = JOptionPane.showInputDialog(frame,message,scriptName,0);
        if(input==null) {
            println("Default option: "+defaultReturn);
            timer.stop();
            return defaultReturn;
        }
        println("Input received: "+input);
        timer.stop();
        return input;
    }

    /**
     * Creates an option pane that requests an int from the user. After set time returns the default option.
     * @param message Prompt for the input (ie. "How old are you?")
     * @param scriptName Name of the script
     * @param defaultReturn Default value to return at specified time
     * @param timeMS Time(ms) before returning default value
     */
    public int optionPaneIntTimed(String message, String scriptName,int defaultReturn, int timeMS) {
        optionPaneOpen = true;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit.getDefaultToolkit().addAWTEventListener(
                new AWTEventListener() {

                    @Override
                    public void eventDispatched(AWTEvent event) {
                        Object source = event.getSource();
                        if (source instanceof Component) {
                            Component comp = (Component) source;
                            Window win = null;
                            if (comp instanceof Window) {
                                win = (Window) comp;
                            } else {
                                win = SwingUtilities.windowForComponent(comp);
                            }
                            if (win == frame) {
                                timer.restart();
                            }
                        }
                    }
                },
                AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK
                        | AWTEvent.MOUSE_MOTION_EVENT_MASK
                        | AWTEvent.MOUSE_WHEEL_EVENT_MASK);

        timer = new Timer(timeMS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        timer.start();
        String input = JOptionPane.showInputDialog(frame, message, scriptName, 0);

        if(frame.isActive())
            frame.dispose();
        optionPaneOpen =false;

        if (input != null){
            int n = Integer.parseInt(input);
            timer.stop();
            return n;
        }
        else {
            println("Default option: "+defaultReturn);
            timer.stop();
            return defaultReturn;
        }
    }

    /**
     * Creates an option pane that requests a Yes/No from the user. After set time returns the default option.
     * @param message Prompt for the input (ie. "Do you like pie?")
     * @param scriptName Name of the script
     * @param defaultReturn Default value to return at specified time
     * @param timeMS Time(ms) before returning default value
     */
    public boolean optionPaneBooleanTimed(String message, String scriptName,boolean defaultReturn, int timeMS) {
        optionPaneOpen = true;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit.getDefaultToolkit().addAWTEventListener(
                new AWTEventListener() {

                    @Override
                    public void eventDispatched(AWTEvent event) {
                        Object source = event.getSource();
                        if (source instanceof Component) {
                            Component comp = (Component) source;
                            Window win = null;
                            if (comp instanceof Window) {
                                win = (Window) comp;
                            } else {
                                win = SwingUtilities.windowForComponent(comp);
                            }
                            if (win == frame) {
                                timer.restart();
                            }
                        }
                    }
                },
                AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK
                        | AWTEvent.MOUSE_MOTION_EVENT_MASK
                        | AWTEvent.MOUSE_WHEEL_EVENT_MASK);

        timer = new Timer(timeMS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        timer.start();
        int n=-1;
        n = JOptionPane.showConfirmDialog(
                frame,
                message,
                "ScriptsForMains: "+scriptName,
                JOptionPane.YES_NO_OPTION);
        if(n==-1){
            println("Default option: "+defaultReturn);
            if(frame.isActive())
                frame.dispose();
            optionPaneOpen =false;
            timer.stop();
            return defaultReturn;
        }

        if(frame.isActive())
            frame.dispose();
        optionPaneOpen = false;
        if (n==0) {
            timer.stop();
            return true;
        }
        else {
            timer.stop();
            return false;
        }
    }

    /**
     * @return True if option pane is open, false otherwise
     */
    public boolean isOptionPaneOpen(){
        return optionPaneOpen;
    }
}