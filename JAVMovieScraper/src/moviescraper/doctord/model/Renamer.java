package moviescraper.doctord.model;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import moviescraper.doctord.Movie;
import moviescraper.doctord.dataitem.Actor;
import moviescraper.doctord.dataitem.Genre;

public class Renamer {

	private String renameString;
	private Movie movie;
	private String sanitizer;
	private File oldFile;
	
	private String extension;
	private String filename;
	private String path;

	private final static String ID = "<ID>";
	private final static String TITLE = "<TITLE>";
	private final static String ACTORS = "<ACTORS>";
	private final static String YEAR = "<YEAR>";
	private final static String ORIGINALTITLE = "<ORIGINALTITLE>";
	private final static String SET = "<SET>";
	private final static String STUDIO = "<STUDIO>";
	private final static String GENRES = "<GENRES>";
	private final static String[] availableRenameTags = {ID, TITLE, ACTORS, GENRES, SET, STUDIO, YEAR, ORIGINALTITLE};
	
	public Renamer(String renameString, String sanitizer, Movie toRename, File oldFile) {
		this.renameString = renameString;
		this.sanitizer = sanitizer;
		this.movie = toRename;
		this.oldFile = oldFile;
	}
	
	public String getNewFileName() {
		extension = FilenameUtils.getExtension(oldFile.toString());
		filename = FilenameUtils.getBaseName(oldFile.toString());
		path = FilenameUtils.getFullPath(oldFile.toString());

		String newName = getSanitizedString (replace());
		newName = path + newName + getAppendix() + "." + extension;
		
		return newName;
	}
	
	private String replace() {
		String movieID = movie.getId().getId();
		String movieTitle = movie.getTitle().getTitle();
		List<Actor> movieActorsList = movie.getActors();
		String movieActors = combineActorList(movieActorsList);
		String movieYear = movie.getYear().getYear();
		String movieOriginalTitle = movie.getOriginalTitle().getOriginalTitle();
		String movieSet = movie.getSet().getSet();
		String movieStudio = movie.getStudio().getStudio();
		String movieGenres = combineGenreList(movie.getGenres());
		String newName = renameString;
		
		
		
		newName = newName.replaceAll(ID, movieID);
		newName = newName.replaceAll(TITLE, movieTitle);
		newName = newName.replaceAll(ACTORS, movieActors);
		newName = newName.replaceAll(YEAR, movieYear);
		newName = newName.replaceAll(ORIGINALTITLE, movieOriginalTitle);
		newName = newName.replaceAll(SET, movieSet);
		newName = newName.replaceAll(STUDIO, movieStudio);
		newName = newName.replaceAll(GENRES, movieGenres);

		return newName;
	}
	
	private String combineActorList(List<Actor> actors) {
		String actorsString = "";
		for (int i = 0; i < movie.getActors().size(); i++) {
			actorsString += movie.getActors().get(i).getName();
			if (i + 1 < movie.getActors().size())
				actorsString += ", ";
		}
		return actorsString;
	}
	
	private String combineGenreList(List<Genre> genres) {
		String genresString = "";
		for (int i = 0; i < movie.getGenres().size(); i++) {
			genresString += movie.getGenres().get(i).getGenre();
			if (i + 1 < movie.getGenres().size())
				genresString += ", ";
		}
		return genresString;
	}
	
	private String getAppendix() {
		String appendix = "";
		boolean hasAppendix = filename.matches(".*CD\\s?1.*");
		if (hasAppendix)
			appendix = "-cd1";
		hasAppendix = filename.matches(".*CD\\s?2.*");
		if (hasAppendix)
			appendix = "-cd2";
		hasAppendix = filename.matches(".*CD\\s?3.*");
		if (hasAppendix)
			appendix = "-cd3";
		
		return appendix;
	}
	
	private String getSanitizedString(String fileName) {
		final Pattern ILLEGAL_CHARACTERS = Pattern.compile(sanitizer);
		fileName = ILLEGAL_CHARACTERS.matcher(fileName).replaceAll("").replaceAll("\\s+", " ").trim();
		return fileName;
	}
	
	public static String getAvailableTags()
	{
		String tags = "";
		for (String tag : availableRenameTags)
		{
			tags= tags + " " + tag;
		}
		System.out.println("tags = " + tags);
		return tags.trim();
	}
	
	//This code was used by the old buggy method and is no longer used
	
	
	/*private String replace(Pattern pattern, String string, String replacement) {
		String match = getMatch(pattern, string);
		String prefix = getPrefix(match, pattern.pattern());
		String suffix = getSuffix(match, pattern.pattern());
		String rep = "";
		if (replacement == null || replacement.isEmpty()) {
			rep = replacement;
		} else {
			rep = prefix + replacement + suffix;
		}
				
		String result = string.replace(match, rep);
		return result;
	}
	
	private String getPrefix(String foundPattern, String pattern) {
		if (foundPattern.length() > 0) {
			String s = foundPattern.substring(1, 2);
			String b = pattern.substring(3,4);
			if (!s.equals( b ))
				return s;
		}
		return "";
	}
	
	private String getSuffix(String foundPattern, String pattern) {
		int length = foundPattern.length();
		if (foundPattern.length() > 0) {
			String s = foundPattern.substring(length - 2,length - 1);
			int patternLength = pattern.length();
			String b = pattern.substring(patternLength-4,patternLength-3);
			if (!s.equals( b ))
				return s;
		}
		return "";
	}
	
	private String substring(String string, int maxLength) {
		if (string.length() > maxLength)
			return string.substring(0, maxLength);
		return string;
	}
	
		Matcher matcher = pattern.matcher(toMatch);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}
	
	*/
	
}
