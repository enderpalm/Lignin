package dev.enderpalm.lignin.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public interface BakedGlyphAccessor {

    /**
     * Set flag value to choose baked glyph render mode, containing: <br>
     * <table>
     *  <tr> <th>ID</th> <th>InBadge</th> <th>BadgeShadow</th> <th>Inflated</th> </tr>
     *  <tr> <td align="center">0</td> <td align="center">False</td> <td align="center">Disabled</td> <td align="center">False</td> </tr>
     *  <tr> <td align="center">1</td> <td align="center">False</td> <td align="center">Disabled</td> <td align="center">True</td> </tr>
     *  <tr> <td align="center">2</td> <td align="center">True</td> <td align="center">False</td> <td align="center">False</td> </tr>
     *  <tr> <td align="center">3</td> <td align="center">True</td> <td align="center">False</td> <td align="center">True</td> </tr>
     *  <tr> <td align="center">4</td> <td align="center">True</td> <td align="center">True</td> <td align="center">False</td> </tr>
     *  <tr> <td align="center">5</td> <td align="center">True</td> <td align="center">Disabled</td> <td align="center">True</td> </tr>
     * </table>
     * @param flag chosen id
     */
    default void setRenderMode(int flag) {
    }

    default int getRenderMode(){
        return 0;
    }

    default boolean isInBadge() {
        return false;
    }

    default boolean isOutline(){
        return false;
    }
}
