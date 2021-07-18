package site.siredvin.progressiveperipherals.common.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import site.siredvin.progressiveperipherals.api.blocks.ITileEntityDataProvider;
import site.siredvin.progressiveperipherals.api.integrations.IProbeable;
import site.siredvin.progressiveperipherals.common.blocks.FlexibleStatue;
import site.siredvin.progressiveperipherals.common.setup.TileEntityTypes;
import site.siredvin.progressiveperipherals.utils.NBTUtils;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;
import site.siredvin.progressiveperipherals.utils.dao.QuadList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlexibleStatueTileEntity extends TileEntity implements ITileEntityDataProvider, IProbeable {
    public static final String BAKED_QUADS_TAG = "bakedQuads";
    public static final String NAME_TAG = "statueName";
    public static final String AUTHOR_TAG = "statueAuthor";
    public static final ModelProperty<QuadList> BAKED_QUADS = new ModelProperty<>();

    private @Nullable QuadList bakedQuads;
    private @Nullable VoxelShape blockShape;
    private @Nullable String name;
    private @Nullable String author;

    public FlexibleStatueTileEntity() {
        super(TileEntityTypes.FLEXIBLE_STATUE.get());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        tag = saveInternalData(tag);
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        QuadList oldBakedQuads = bakedQuads;
        CompoundNBT tag = pkt.getTag();
        if (tag.contains(BAKED_QUADS_TAG)) {
            QuadList newBakedQuads = NBTUtils.readQuadList(tag.getByteArray(BAKED_QUADS_TAG));
            if (!Objects.equals(oldBakedQuads, newBakedQuads)) {
                if (newBakedQuads != null) {
                    setBakedQuads(newBakedQuads);
                } else {
                    clear();
                }
                ModelDataManager.requestModelDataRefresh(this);
            }
        }
        if (tag.contains(NAME_TAG))
            setName(tag.getString(NAME_TAG));
        if (tag.contains(AUTHOR_TAG))
            setAuthor(tag.getString(AUTHOR_TAG));
    }

    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(BAKED_QUADS, bakedQuads).build();
    }

    public void loadInternalData(BlockState state, CompoundNBT tag, boolean skipUpdate) {
        if (tag.contains(BAKED_QUADS_TAG)) {
            QuadList newBakedQuads = NBTUtils.readQuadList(tag.getByteArray(BAKED_QUADS_TAG));
            if (newBakedQuads != null)
                setBakedQuads(newBakedQuads, skipUpdate);
        }
        if (tag.contains(NAME_TAG))
            setName(tag.getString(NAME_TAG));
        if (tag.contains(AUTHOR_TAG))
            setAuthor(tag.getString(AUTHOR_TAG));
    }

    public CompoundNBT saveInternalData(CompoundNBT tag) {
        if (bakedQuads != null) {
            INBT data = NBTUtils.serialize(bakedQuads);
            if (data != null)
                tag.put(BAKED_QUADS_TAG, data);
        }
        if (name != null) {
            tag.putString(NAME_TAG, name);
        }
        if (author != null) {
            tag.putString(AUTHOR_TAG, author);
        }
        return tag;
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        loadInternalData(state, tag, true);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag = saveInternalData(tag);
        return super.save(tag);
    }

    public void setBakedQuads(@Nonnull QuadList bakedQuads) {
        setBakedQuads(bakedQuads, false);
    }

    public void setBakedQuads(@Nonnull QuadList bakedQuads, boolean skipUpdate) {
        this.bakedQuads = bakedQuads;
        refreshShape();
        if (!skipUpdate)
            pushState(getBlockState().setValue(FlexibleStatue.CONFIGURED, true));
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public void setAuthor(@Nonnull String author) {
        this.author = author;
    }

    public @Nullable String getName() {
        return name;
    }

    public @Nullable String getAuthor() {
        return author;
    }

    public void clear() {
        bakedQuads = null;
        refreshShape();
        pushState(getBlockState().setValue(FlexibleStatue.CONFIGURED, false));
    }

    public void refreshShape() {
        if (bakedQuads == null) {
            blockShape = null;
        } else {
            blockShape = bakedQuads.shape();
        }
    }

    public void pushState(BlockState state) {
        setChanged();
        level.setBlockAndUpdate(getBlockPos(), state);
        level.sendBlockUpdated(
                worldPosition, getBlockState(), getBlockState(),
                Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.RERENDER_MAIN_THREAD
        );
    }

    public @Nullable VoxelShape getBlockShape() {
        return blockShape;
    }

    public @Nullable QuadList getBakedQuads() {
        return bakedQuads;
    }


    @Override
    public List<ITextComponent> commonProbeData(BlockState state) {
        List<ITextComponent> data = new ArrayList<>();
        if (name != null)
            data.add(TranslationUtil.localization("statue_name").append(name));
        if (author != null)
            data.add(TranslationUtil.localization("statue_author").append(author));
        return data;
    }
}
