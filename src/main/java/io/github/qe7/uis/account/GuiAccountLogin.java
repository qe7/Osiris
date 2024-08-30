package io.github.qe7.uis.account;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import org.lwjgl.input.Keyboard;

public class GuiAccountLogin extends GuiScreen {

    protected GuiScreen parentScreen;

    private GuiTextField usernameField;

    private GuiButton loginButton;

    public GuiAccountLogin(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        this.controlList.add(this.loginButton = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Login"));
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 144, "Back"));

        Keyboard.enableRepeatEvents(true);

        this.usernameField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, this.height / 4 + 96, 200, 20);
        this.usernameField.setMaxStringLength(16);
        this.usernameField.func_50033_b(true);
        this.usernameField.setText(Minecraft.getMinecraft().session.username == null ? "Username" : Minecraft.getMinecraft().session.username);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        this.usernameField.updateCursorCounter();
        this.loginButton.enabled = this.usernameField.getText().length() > 3 && this.usernameField.getText().length() < 16 && !this.usernameField.getText().contains(" ");
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();

        this.drawCenteredString(this.fontRenderer, "Account Login", this.width / 2, 40, 0xFFFFFF);
        this.drawCenteredString(this.fontRenderer, "Username: " + Minecraft.getMinecraft().session.username, this.width / 2, 50, 0xA0A0A0);

        this.usernameField.drawTextBox();

        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        super.keyTyped(par1, par2);

        this.usernameField.func_50037_a(par1, par2);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        super.actionPerformed(guibutton);

        switch (guibutton.id) {
            case 0:
                if (this.usernameField.getText().isEmpty()) {
                    return;
                }
                if (this.usernameField.getText().length() > 16 || this.usernameField.getText().length() < 3) {
                    return;
                }
                if (this.usernameField.getText().contains(" ")) {
                    return;
                }
                Minecraft.getMinecraft().session.username = this.usernameField.getText();
                break;
            case 1:
                Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
                break;
        }
    }
}
