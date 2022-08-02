package com.paragon.client.systems.module.impl.render

import com.paragon.api.event.render.entity.RenderArmEvent
import com.paragon.api.module.Category
import com.paragon.api.module.Module
import com.paragon.api.setting.Setting
import com.paragon.api.util.Wrapper
import com.paragon.asm.mixins.accessor.IEntityRenderer
import com.paragon.client.shader.shaders.FluidShader
import me.wolfsurge.cerauno.listener.Listener
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.model.ModelPlayer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.shader.Framebuffer
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20
import java.awt.Color

/**
 * @author Surge
 * @since 01/08/2022
 */
object HandChams : Module("HandChams", Category.RENDER, "Changes the hand colour") {

    private val mode = Setting("Mode", Mode.WIRE_MODEL)
        .setDescription("The render mode to use")

    private val texture = Setting("Texture", false)
        .setDescription("Render the entity's texture")

    private val lighting = Setting("Lighting", true)
        .setDescription("Disables lighting")

    private val blend = Setting("Blend", true)
        .setDescription("Enables blending")

    private val transparent = Setting("Transparent", true)
        .setDescription("Enables transparency on models")

    private val width = Setting("Width", 1f, 0.1f, 3f, 0.1f)
        .setDescription("The width of the outline")
        .setVisibility { mode.value != Mode.MODEL }

    private val colour = Setting("Colour", Color(185, 17, 255))
        .setDescription("The colour of the hand")
    
    @Listener
    fun onRenderLeftPre(event: RenderArmEvent.LeftArmPre) {
        if (event.player == minecraft.player) {
            // Enable transparency
            if (transparent.value) {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL)
            }

            glPushAttrib(GL_ALL_ATTRIB_BITS)
            glDisable(GL_ALPHA_TEST)

            // Enable blend
            if (blend.value) {
                glEnable(GL_BLEND)
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            }

            // Disable texture
            if (!texture.value) {
                glDisable(GL_TEXTURE_2D)
            }

            // Disable lighting
            if (lighting.value) {
                glDisable(GL_LIGHTING)
            }

            glEnable(GL_LINE_SMOOTH)
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST)

            glLineWidth(width.value)
            glEnable(GL_STENCIL_TEST)
            glEnable(GL_POLYGON_OFFSET_LINE)

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)

            glColor4f(colour.value.red / 255f, colour.value.green / 255f, colour.value.blue / 255f, colour.alpha / 255f)

            // Change polygon rendering mode
            when (mode.value) {
                Mode.WIRE -> glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
                Mode.WIRE_MODEL, Mode.MODEL -> glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
            }

            if (mode.value == Mode.WIRE_MODEL) {
                glColor4f(colour.value.red / 255f, colour.value.green / 255f, colour.value.blue / 255f, colour.alpha / 255f)

                glLineWidth(width.value)
                renderLeftArm(event.player, event.useSmallArms)
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
            }
        }
    }

    @Listener
    fun onRenderLeftPost(event: RenderArmEvent.LeftArmPost) {
        if (event.player == minecraft.player) {
            // Enable lighting
            if (lighting.value) {
                glEnable(GL_LIGHTING)
            }

            // Enable blending
            if (blend.value) {
                glDisable(GL_BLEND)
            }

            // Enable texture
            if (!texture.value) {
                glEnable(GL_TEXTURE_2D)
            }

            // Enable transparency
            if (transparent.value) {
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL)
            }

            glPopAttrib()
        }
    }

    @Listener
    fun onRenderRightPre(event: RenderArmEvent.RightArmPre) {
        if (event.player == minecraft.player) {
            // Enable transparency
            if (transparent.value) {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL)
            }

            glPushAttrib(GL_ALL_ATTRIB_BITS)
            glDisable(GL_ALPHA_TEST)

            // Enable blend
            if (blend.value) {
                glEnable(GL_BLEND)
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
            }

            // Disable texture
            if (!texture.value) {
                glDisable(GL_TEXTURE_2D)
            }

            // Disable lighting
            if (lighting.value) {
                glDisable(GL_LIGHTING)
            }

            glEnable(GL_LINE_SMOOTH)
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST)

            glLineWidth(width.value)
            glEnable(GL_STENCIL_TEST)
            glEnable(GL_POLYGON_OFFSET_LINE)

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)

            glColor4f(colour.value.red / 255f, colour.value.green / 255f, colour.value.blue / 255f, colour.alpha / 255f)

            // Change polygon rendering mode
            when (mode.value) {
                Mode.WIRE -> glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
                Mode.WIRE_MODEL, Mode.MODEL -> glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
            }

            if (mode.value == Mode.WIRE_MODEL) {
                glColor4f(colour.value.red / 255f, colour.value.green / 255f, colour.value.blue / 255f, colour.alpha / 255f)

                renderRightArm(event.player, event.useSmallArms)

                glLineWidth(width.value)
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
            }
        }
    }

    @Listener
    fun onRenderRightPost(event: RenderArmEvent.RightArmPost) {
        if (event.player == minecraft.player) {
            // Enable lighting
            if (lighting.value) {
                glEnable(GL_LIGHTING)
            }

            // Enable blending
            if (blend.value) {
                glDisable(GL_BLEND)
            }

            // Enable texture
            if (!texture.value) {
                glEnable(GL_TEXTURE_2D)
            }

            // Enable transparency
            if (transparent.value) {
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL)
            }

            glPopAttrib()
        }
    }

    private fun renderRightArm(clientPlayer: EntityPlayer?, useSmallArms: Boolean) {
        glColor4f(colour.value.red / 255f, colour.value.green / 255f, colour.value.blue / 255f, colour.alpha / 255f)

        val modelPlayer = ModelPlayer(0.0f, useSmallArms)

        GlStateManager.enableBlend()

        modelPlayer.swingProgress = 0.0f
        modelPlayer.isSneak = false

        if (clientPlayer != null) {
            modelPlayer.setRotationAngles(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, clientPlayer)
        }

        modelPlayer.bipedRightArm.rotateAngleX = 0.0f

        modelPlayer.bipedRightArm.render(0.0625f)

        modelPlayer.bipedRightArmwear.rotateAngleX = 0.0f
        modelPlayer.bipedRightArmwear.render(0.0625f)

        GlStateManager.disableBlend()
    }

    private fun renderLeftArm(clientPlayer: EntityPlayer?, useSmallArms: Boolean) {
        glColor4f(colour.value.red / 255f, colour.value.green / 255f, colour.value.blue / 255f, colour.alpha / 255f)

        val modelPlayer = ModelPlayer(0.0f, useSmallArms)

        GlStateManager.enableBlend()

        modelPlayer.isSneak = false
        modelPlayer.swingProgress = 0.0f

        if (clientPlayer != null) {
            modelPlayer.setRotationAngles(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, clientPlayer)
        }

        modelPlayer.bipedLeftArm.rotateAngleX = 0.0f
        modelPlayer.bipedLeftArm.render(0.0625f)
        modelPlayer.bipedLeftArmwear.rotateAngleX = 0.0f
        modelPlayer.bipedLeftArmwear.render(0.0625f)

        GlStateManager.disableBlend()
    }

    enum class Mode {
        /**
         * Renders the model
         */
        MODEL,

        /**
         * Outlines the model
         */
        WIRE,

        /**
         * Renders and outlines the model
         */
        WIRE_MODEL
    }

}