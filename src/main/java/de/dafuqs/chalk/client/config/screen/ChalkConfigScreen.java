package de.dafuqs.chalk.client.config.screen;

import de.dafuqs.chalk.client.config.ChalkConfigHelper;
import de.dafuqs.chalk.common.data.ChalkCompatibilityData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class ChalkConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    public ChalkConfigScreen(Screen PARENT) {
        super(Text.translatable("gui.chalk.config.title").formatted(Formatting.BOLD));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
    }
    public void init() {
        GRID.getMainPositioner().alignHorizontalCenter().margin(0);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        GRID_ADDER.add(createTitle());
        GRID_ADDER.add(createEmitParticles());
        GRID_ADDER.add(createAddons());
        GRID_ADDER.add(createFooter());
        GRID.refreshPositions();
        GRID.forEachChild(this::addDrawableChild);
        initTabNavigation();
    }

    public void tick() {
        if (this.SHOULD_CLOSE) {
            ChalkConfigHelper.saveConfig(false);
            client.setScreen(PARENT_SCREEN);
        }
    }
    private GridWidget createTitle() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(new TextWidget(this.title, this.textRenderer));
        return GRID;
    }
    private GridWidget createEmitParticles() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Text.translatable("gui.chalk.config.emit_particles", ChalkConfigHelper.getConfig("emit_particles")), (button) -> {
            ChalkConfigHelper.setConfig("emit_particles", !(boolean)ChalkConfigHelper.getConfig("emit_particles"));
            client.setScreen(new ChalkConfigScreen(PARENT_SCREEN));
        }).build()).setTooltip(Tooltip.of(Text.translatable("gui.chalk.config.emit_particles.hover"), Text.translatable("gui.chalk.config.emit_particles.hover")));
        return GRID;
    }
    private GridWidget createAddons() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
        /* If a future addon gets created, add a check for it in the following if statement. */
        if (!(ChalkCompatibilityData.COLORFUL_ADDON)) {
            GRID_ADDER.add(new TextWidget(Text.translatable("gui.chalk.config.addons"), this.textRenderer));
            if (!ChalkCompatibilityData.COLORFUL_ADDON) {
                GRID_ADDER.add(ButtonWidget.builder(
                        Text.translatable("gui.chalk.config.addons.colorful_addon"),
                        ConfirmLinkScreen.opening("https://modrinth.com/mod/chalk-colorful-addon",
                                this,
                                true)
                ).build()).setTooltip(Tooltip.of(Text.translatable("gui.chalk.config.addons.colorful_addon.hover")));
            }
        }
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(
                ButtonWidget.builder(Text.translatable("gui.chalk.config.reset"), (button) -> {
                    ChalkConfigHelper.resetConfig();
                    client.setScreen(new ChalkConfigScreen(PARENT_SCREEN));
                }).build()
        ).setTooltip(Tooltip.of(Text.translatable("gui.chalk.config.reset.hover")));
        GRID_ADDER.add(
                ButtonWidget.builder(Text.translatable("gui.chalk.config.back"), (button) -> {
                    this.SHOULD_CLOSE = true;
                }).build()
        ).setTooltip(Tooltip.of(Text.translatable("gui.chalk.config.back.hover")));
        return GRID;
    }
    public void initTabNavigation() {
        SimplePositioningWidget.setPos(GRID, getNavigationFocus());
    }
    public boolean shouldCloseOnEsc() {
        return false;
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) this.SHOULD_CLOSE = true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
}