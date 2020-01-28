package framework;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class Giffer
{
    private ImageWriter writer;
    private ImageWriteParam params;
    private IIOMetadata metadata;

    private Giffer(ImageOutputStream out, int imageType, int delay, boolean loop) throws IOException {
        writer = ImageIO.getImageWritersBySuffix("gif").next();
        params = writer.getDefaultWriteParam();

        ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
        metadata = writer.getDefaultImageMetadata(imageTypeSpecifier, params);

        configureRootMetadata(delay, loop);

        writer.setOutput(out);
        writer.prepareWriteSequence(null);
    }

    public static void createGif(String fileName)
    {
        try {

            BufferedImage first = ImageIO.read(new File(TestConfig.pathToActual() + fileName + ".png"));
            ImageOutputStream output = new FileImageOutputStream(new File(TestConfig.pathToGif() + fileName + ".gif"));

            Giffer writer = new Giffer(output, first.getType(), 500, true);
            writer.writeToSequence(first);

            File[] images = new File[]{
                    new File(TestConfig.pathToExpected() + fileName + ".png"),
                    new File(TestConfig.pathToActual() + fileName + ".png"),
                    new File(TestConfig.pathToDiff()+ fileName + ".png"),
            };

            for (File image : images) {
                BufferedImage next = ImageIO.read(image);
                writer.writeToSequence(next);
            }

            writer.close();
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToSequence(RenderedImage img) throws IOException {
        writer.writeToSequence(new IIOImage(img, null, metadata), params);
    }

    private void close() throws IOException {
        writer.endWriteSequence();
    }

    private void configureRootMetadata(int delay, boolean loop) throws IIOInvalidTreeException {
        String metaFormatName = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);

        IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delay / 10));
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

        IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
        commentsNode.setAttribute("CommentExtension", "Created by: https://memorynotfound.com");

        IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");

        int loopContinuously = loop ? 0 : 1;
        child.setUserObject(new byte[]{ 0x1, (byte) (loopContinuously & 0xFF), (byte) ((loopContinuously >> 8) & 0xFF)});
        appExtensionsNode.appendChild(child);
        metadata.setFromTree(metaFormatName, root);
    }

    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) {
            if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                return (IIOMetadataNode) rootNode.item(i);
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return (node);
    }
}