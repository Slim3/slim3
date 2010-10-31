package org.slim3.eclipse.core.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;

@SuppressWarnings( { "unchecked", "restriction" })
public class RelativityFileSystemStructureProvider implements
		IImportStructureProvider {
	private File root;

	public RelativityFileSystemStructureProvider(File root) {
		this.root = root;
	}
	public RelativityFileSystemStructureProvider(String basepath, String name) {
		this(new File(basepath, name));
	}

	public File getRoot() {
		return root;
	}

	public List getChildren(Object element) {
		File folder = (File) element;
		String[] children = folder.list();
		int childrenLength = children == null ? 0 : children.length;
		List result = new ArrayList(childrenLength);

		for (int i = 0; i < childrenLength; i++) {
			result.add(new File(folder, children[i]));
		}

		return result;
	}

	public InputStream getContents(Object element) {
		try {
			return new FileInputStream((File) element);
		} catch (FileNotFoundException e) {
			IDEWorkbenchPlugin.log(e.getLocalizedMessage(), e);
		}
		return null;
	}

	private String stripPath(String path) {
		int index = path.indexOf(root.getName());
		path = path.substring(index + root.getName().length());
		return path;
	}

	public String getFullPath(Object element) {
		return stripPath(((File) element).getPath());
	}

	public String getLabel(Object element) {
		File file = (File) element;
		String name = file.getName();
		if (name.length() == 0) {
			return file.getPath();
		}
		return name;
	}

	public boolean isFolder(Object element) {
		return ((File) element).isDirectory();
	}
}
