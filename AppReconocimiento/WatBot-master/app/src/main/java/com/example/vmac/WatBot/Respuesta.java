package com.example.vmac.WatBot;

import com.ibm.watson.developer_cloud.conversation.v1.model.Context;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageInput;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.conversation.v1.model.OutputData;

import java.util.ArrayList;

public class Respuesta {
    public ArrayList intents;
    public MessageInput input;
    public OutputData output;
    public Context context;
    public ArrayList entities;

    private MessageResponse response;

    public Respuesta() {
    }

    public Respuesta(ArrayList intents, MessageInput input, OutputData output, Context context, ArrayList entities) {
        this.intents = intents;
        this.input = input;
        this.output = output;
        this.context = context;
        this.entities = entities;
    }

    public Respuesta(MessageResponse response) {
        this.response = response;
    }

    public ArrayList getIntents() {
        return intents;
    }

    public void setIntents(ArrayList intents) {
        this.intents = intents;
    }

    public MessageInput getInput() {
        return input;
    }

    public void setInput(MessageInput input) {
        this.input = input;
    }

    public OutputData getOutput() {
        return output;
    }

    public void setOutput(OutputData output) {
        this.output = output;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList getEntities() {
        return entities;
    }

    public void setEntities(ArrayList entities) {
        this.entities = entities;
    }

    public String Accion() {
        setIntents((ArrayList) response.getIntents());
        setEntities((ArrayList) response.getEntities());
        setContext(response.getContext());
        setInput(response.getInput());
        setOutput(response.getOutput());
        return toString();

    }

    @Override
    public String toString() {
        return "Respuesta{" +
                "intents=" + intents +
                ", input=" + input +
                ", output=" + output +
                ", context=" + context +
                ", entities=" + entities +
                '}';
    }
}
