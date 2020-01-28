package framework;

import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.comparison.PointsMarkupPolicy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Differ
{

    public static int getDiffSize(Screenshot expected, Screenshot actual)
    {
        ImageDiff diff = new ImageDiffer().makeDiff(expected, actual);
        return diff.getDiffSize();
    }

    public static void saveDiffImage(Screenshot expected, Screenshot actual, String fileName)
    {
        ImageDiffer imageDiffer = new ImageDiffer().withDiffMarkupPolicy(new PointsMarkupPolicy().withDiffColor(Color.RED));
        ImageDiff diff = imageDiffer.makeDiff(expected, actual);

        File diffFile = new File(TestConfig.pathToDiff() + fileName +  ".png");

        try {
            ImageIO.write(diff.getMarkedImage(), "png", diffFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
