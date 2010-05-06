/*
 * Copyright (c) 2008, 2009, 2010 Denis Tulskiy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tulskiy.musique.system;

import com.tulskiy.musique.audio.AudioFileReader;
import com.tulskiy.musique.audio.AudioTagWriter;
import com.tulskiy.musique.audio.Decoder;
import com.tulskiy.musique.audio.formats.aac.MP4FileReader;
import com.tulskiy.musique.audio.formats.aac.MP4TagWriter;
import com.tulskiy.musique.audio.formats.ape.APEFileReader;
import com.tulskiy.musique.audio.formats.ape.APETagWriter;
import com.tulskiy.musique.audio.formats.cue.CUEFileReader;
import com.tulskiy.musique.audio.formats.flac.FLACFileReader;
import com.tulskiy.musique.audio.formats.ogg.VorbisTagWriter;
import com.tulskiy.musique.audio.formats.mp3.MP3FileReader;
import com.tulskiy.musique.audio.formats.mp3.MP3TagWriter;
import com.tulskiy.musique.audio.formats.ogg.OGGFileReader;
import com.tulskiy.musique.audio.formats.uncompressed.PCMFileReader;
import com.tulskiy.musique.audio.formats.wavpack.WavPackFileReader;
import com.tulskiy.musique.playlist.Song;
import com.tulskiy.musique.util.Util;

import java.util.ArrayList;

//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.util.ArrayList;
//import java.util.ServiceLoader;

/**
 * @Author: Denis Tulskiy
 * @Date: 24.06.2009
 */
public class PluginLoader {
    private static ArrayList<AudioFileReader> readers;
    private static ArrayList<AudioTagWriter> writers;

    static {
        readers = new ArrayList<AudioFileReader>();
        MP3FileReader mp3Reader = new MP3FileReader();
//        mp3Reader.setUseNativeDecoder(true);
        readers.add(mp3Reader);
        readers.add(new MP4FileReader());
        readers.add(new APEFileReader());
        readers.add(new CUEFileReader());
        readers.add(new FLACFileReader());
        readers.add(new OGGFileReader());
        readers.add(new PCMFileReader());
        readers.add(new WavPackFileReader());

        writers = new ArrayList<AudioTagWriter>();
        writers.add(new MP3TagWriter());
        writers.add(new APETagWriter());
        writers.add(new VorbisTagWriter());
        writers.add(new MP4TagWriter());
    }

    public static AudioFileReader getAudioFileReader(String fileName) {
        String ext = Util.getFileExt(fileName);
        for (AudioFileReader reader : readers) {
            if (reader.isFileSupported(ext))
                return reader;
        }

        return null;
    }

    public static AudioTagWriter getAudioFileWriter(String fileName) {
        String ext = Util.getFileExt(fileName);
        for (AudioTagWriter writer : writers) {
            if (writer.isFileSupported(ext))
                return writer;
        }

        return null;
    }

    //    private static ServiceLoader<AudioFileReader> audioFileReaderServiceLoader = null;

//    public static AudioFileReader getAudioFileReader(String ext) {
//        if (audioFileReaderServiceLoader == null) {
//            loadSPI();
//        }
//        for (AudioFileReader fileReader : audioFileReaderServiceLoader) {
//            if (fileReader.isFileSupported(ext))
//                return fileReader;
//        }
//        return null;
//    }

//    private static void loadSPI() {
//        File pluginsDir = new File("plugins");
//        ArrayList<URL> urls = new ArrayList<URL>();
//        for (File f : pluginsDir.listFiles()) {
//            if (Util.getFileExt(f).equalsIgnoreCase("jar")) {
//                try {
//                    urls.add(f.toURI().toURL());
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        URLClassLoader pluginClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), ClassLoader.getSystemClassLoader());
//        audioFileReaderServiceLoader = ServiceLoader.load(AudioFileReader.class, pluginClassLoader);
//    }

    public static Decoder getDecoder(Song audioFile) {
        return getAudioFileReader(audioFile.getFile().getName()).getDecoder();
    }
}
