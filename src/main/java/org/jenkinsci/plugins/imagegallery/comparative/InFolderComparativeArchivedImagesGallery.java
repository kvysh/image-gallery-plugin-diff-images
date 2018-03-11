/*
 * The MIT License
 *
 * Copyright (c) <2012> <Richard Lavoie>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.imagegallery.comparative;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Actionable;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.Project;
import hudson.tasks.BuildStepCompatibilityLayer;
import hudson.tasks.BuildStepMonitor;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.imagegallery.comparative.ComparativeImagesGalleryProjectAction;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * An image gallery of archived artifacts Comparing same files in different folders.
 *
 * @author Richard Lavoie
 * @since 1.0
 */
public class InFolderComparativeArchivedImagesGallery extends ComparativeArchivedImagesGallery {

	private static final long serialVersionUID = -1981209232197421074L;

	/**
	 * Constructor called from jelly.
	 * 
	 * @param title Title of the image gallery
	 * @param baseRootFolder Root folder where the images will be retrieve from
	 * @param imageWidth thumbnail width of each images
	 * @param imageInnerWidth Width for the images in the popup display
	 * @param markBuildAsUnstableIfNoArchivesFound Mark the build as unstable if no archives found
	 */
	@DataBoundConstructor
	public InFolderComparativeArchivedImagesGallery(String title, String baseRootFolder, Integer imageWidth, Integer imageInnerWidth,
                                                    boolean markBuildAsUnstableIfNoArchivesFound) {
		super(title, baseRootFolder, imageWidth, imageInnerWidth, markBuildAsUnstableIfNoArchivesFound);
	}
	
	@Extension
	public static class DescriptorImpl extends ComparativeDescriptorImpl {
		@Override
		public String getDisplayName() {
			return "In folder comparative archived images gallery";
		}

	}

	@Override
	public boolean createImageGallery(AbstractBuild<?, ?> build, BuildListener listener) throws InterruptedException, IOException {
		listener.getLogger().append("Creating archived images gallery.");
		EnvVars envVars = build.getEnvironment(listener);
		
		if (build.getHasArtifacts()) {
			//File artifactsDir = build.getArtifactsDir().getAbsoluteFile();
			//FilePath artifactsPath = new FilePath(new File(artifactsDir.getAbsoluteFile(), getBaseRootFolder()));
			FilePath testingPath;
			if(!getBaseRootFolder().isEmpty()) {
				testingPath = build.getWorkspace().withSuffix("/"+getBaseRootFolder());
			} else {
				testingPath = build.getWorkspace().withSuffix("/");
			}
			//FilePath[] files = artifactsPath.list("**");
			FilePath[] files = testingPath.list("**");
			
			if (files != null && files.length > 0) {
				FilePairTree tree = new FilePairTree();
				for (FilePath path : files) {
					List<String> folder = getRelativeFrom(path.getParent(), testingPath);
					List<String> artifactsRelativeFile = getRelativeFrom(path, testingPath.getParent());
					List<String> myList = new ArrayList<String>();
					myList.add(0, getBaseRootFolder().toString());
					for(int i=1; i<artifactsRelativeFile.size(); i++) {
						myList.add(i, artifactsRelativeFile.get(i));
					}
					tree.addToBranch(folder, new FilePair(path.getName(), StringUtils.join(myList, '/')));
				}
				
				String title = Util.replaceMacro(build.getEnvironment(listener).expand(getTitle()), build.getBuildVariableResolver());
				build.addAction(new ComparativeImagesGalleryBuildAction(title, tree, getImageWidth(), getImageInnerWidth()));
				build.save();
			} else {
				listener.getLogger().append("No files found for image gallery.");
			}
		} else {
			if(getBaseRootFolder() != null) {
				//FilePath artifactsPath = new FilePath(new File(build.getWorkspace()+"/"+getBaseRootFolder()));
				//FilePath testingPath = build.getWorkspace().withSuffix(getBaseRootFolder());
				FilePath testingPath;
				if(!getBaseRootFolder().isEmpty()) {
					testingPath = build.getWorkspace().withSuffix("/"+getBaseRootFolder());
				} else {
					testingPath = build.getWorkspace().withSuffix("/");
				}
				//FilePath[] files = artifactsPath.list("**");
				FilePath[] files = testingPath.list("**");
			
				if (files != null && files.length > 0) {
					FilePairTree tree = new FilePairTree();
					for (FilePath path : files) {
						List<String> folder = getRelativeFrom(path.getParent(), testingPath);
						List<String> artifactsRelativeFile = getRelativeFrom(path, testingPath.getParent());
						List<String> myList = new ArrayList<String>();
						myList.add(0, getBaseRootFolder().toString());
						for(int i=1; i<artifactsRelativeFile.size(); i++) {
							myList.add(i, artifactsRelativeFile.get(i));
						}
						tree.addToBranch(folder, new FilePair(path.getName(), StringUtils.join(myList, '/')));
					}
					String title = Util.replaceMacro(build.getEnvironment(listener).expand(getTitle()), build.getBuildVariableResolver());
					build.addAction(new ComparativeImagesGalleryBuildAction(title, tree, getImageWidth(), getImageInnerWidth()));
					build.save();
				} else {
					listener.getLogger().append("No files found for image gallery from InFolder.");
				}
			} else {
				if(isMarkBuildAsUnstableIfNoArchivesFound()) {
					build.setResult(Result.UNSTABLE);
				}
				listener.getLogger().append("This build has no artifacts. Skipping image gallery in this build.");
			}
		}
		return true;
	}
	
	@Override
	public Action getActionFromBuild(AbstractProject<?, ?> project) throws Exception {
		System.out.println("In getActionFromBuild method");
        Action collection = null;
        collection = new ComparativeImagesGalleryProjectAction(getTitle(), null, getImageWidth(), getImageInnerWidth(), project);
		return collection;
	}
}
