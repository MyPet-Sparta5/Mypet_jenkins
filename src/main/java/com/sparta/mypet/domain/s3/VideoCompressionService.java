package com.sparta.mypet.domain.s3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.InvalidFileException;

@Service
public class VideoCompressionService {

	@Value("${ffmpeg.location}")
	private String ffmpegPath;

	@Value("${ffprobe.location}")
	private String ffprobePath;

	@Value("${temp.file.path}")
	private String tempFilePath;

	private static final int VIDEO_BITRATE = 10000000;  // 비트레이트 설정 (대략 10Mbps)

	public String compressVideo(MultipartFile file) {
		try {
			String filePath = saveTempFile(file);
			String compressedFilePath = compressVideoFile(filePath);
			Files.delete(new File(filePath).toPath());
			return compressedFilePath;
		} catch (IOException e) {
			throw new InvalidFileException(GlobalMessage.PROCESSING_FILE_FAILED.getMessage());
		}
	}

	private String saveTempFile(MultipartFile file) throws IOException {
		String uniqueId = UUID.randomUUID().toString();
		String fileName = file.getOriginalFilename();
		String filePath = tempFilePath + uniqueId + "_" + fileName;

		File tempFile = new File(filePath);
		tempFile.getParentFile().mkdirs();
		file.transferTo(tempFile);

		return filePath;
	}

	private String compressVideoFile(String filePath) {
		String fileName = new File(filePath).getName();
		String uniqueId = UUID.randomUUID().toString();
		String compressedFilePath = tempFilePath + uniqueId + "_compressed_" + fileName;

		try {
			FFmpegBuilder builder = new FFmpegBuilder()
				.setInput(filePath)
				.addOutput(compressedFilePath)
				.setFormat("mp4")
				.disableSubtitle()
				.setVideoCodec("libx264")
				.setAudioCodec("aac")
				.setVideoBitRate(VIDEO_BITRATE)
				.setVideoFrameRate(30)
				.done();

			FFmpegExecutor executor = new FFmpegExecutor(new FFmpeg(ffmpegPath), new FFprobe(ffprobePath));
			executor.createJob(builder).run();

			File compressedFile = new File(compressedFilePath);
			if (!compressedFile.exists()) {
				throw new InvalidFileException(GlobalMessage.PROCESSING_FILE_FAILED.getMessage());
			}
		} catch (IOException e) {
			throw new InvalidFileException(GlobalMessage.PROCESSING_FILE_FAILED.getMessage());
		}

		return compressedFilePath;
	}
}
