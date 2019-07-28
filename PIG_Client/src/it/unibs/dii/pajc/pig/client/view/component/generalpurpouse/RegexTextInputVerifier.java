package it.unibs.dii.pajc.pig.client.view.component.generalpurpouse;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that implements the input verification by regex.
 * It's possible to set an action on success or failure of the verification and if the failure locks focus of the component,
 */
public class RegexTextInputVerifier extends InputVerifier {
    private Pattern p;
    private VerificationAction success, failure;
    private boolean lockOnFailure = false;

    public RegexTextInputVerifier(String regex) {
        super();

        p = Pattern.compile(regex);
    }

    public void setSuccessAction(VerificationAction success) {
        this.success = success;
    }

    public void setFailureAction(VerificationAction failure) {
        this.failure = failure;
    }

    public void setLockOnFailure(boolean lockOnFailure) {
        this.lockOnFailure = lockOnFailure;
    }

    public VerificationAction getSuccessAction() {
        return success;
    }

    public VerificationAction getFailureAction() {
        return failure;
    }

    public boolean isLockOnFailure() {
        return lockOnFailure;
    }

    @Override
    public boolean verify(JComponent input) {
        if (input instanceof JTextComponent) {
            JTextComponent txt = (JTextComponent)input;
            String line = txt.getText();
            Matcher m = p.matcher(line);
            return m.matches();
        }

        return true;
    }

    @Override
    public boolean shouldYieldFocus(JComponent source, JComponent target) {
        boolean esit = super.shouldYieldFocus(source, target);

        //If success and success action is setted then exec the action
        if (esit && success != null)
            success.execute(source);
        //If failure and failure action is setted then exec the action
        if (!esit && failure != null)
            failure.execute(source);

        //If lockOnFailure == false then method will return always true, so focus is not locked on the component
        return esit || !lockOnFailure;
    }


    /**
     * Interface to set an action on verification for the input.
     */
    public interface VerificationAction {
        void execute (JComponent input);
    }
}
