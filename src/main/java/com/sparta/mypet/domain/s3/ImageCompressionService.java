package com.sparta.mypet.domain.s3;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.mypet.domain.s3.entity.FileContentType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageCompressionService {

	private static final int TARGET_HEIGHT = 650;

	public ByteArrayInputStream compressImage(MultipartFile file, FileContentType type) throws IOException {
		BufferedImage processedImage = resizeImage(file);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String formatName = type.getType().split("/")[1];
		ImageIO.write(processedImage, formatName, baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}

	private BufferedImage resizeImage(MultipartFile multipartFile) throws IOException {
		BufferedImage sourceImage = ImageIO.read(multipartFile.getInputStream());
		if (sourceImage.getHeight() <= TARGET_HEIGHT) {
			return sourceImage;
		}
		double sourceImageRatio = (double)sourceImage.getWidth() / sourceImage.getHeight();
		int newWidth = (int)(TARGET_HEIGHT * sourceImageRatio);
		return Scalr.resize(sourceImage, newWidth, TARGET_HEIGHT);
	}
}
