package com.paragon.asm.mixins.world.block;

import com.paragon.Paragon;
import com.paragon.api.event.world.LiquidInteractEvent;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLiquid.class)
public class MixinBlockLiquid {

    @Inject(method = "canCollideCheck", at = @At("HEAD"), cancellable = true)
    public void onCanCollideCheck(IBlockState state, boolean hitIfLiquid, CallbackInfoReturnable<Boolean> cir) {
        LiquidInteractEvent liquidInteractEvent = new LiquidInteractEvent();
        Paragon.INSTANCE.getEventBus().post(liquidInteractEvent);

        if (liquidInteractEvent.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

}
