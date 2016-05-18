package org.tldgen;

import org.tldgen.annotations.License;
import org.tldgen.annotations.TldVersion;

public class DocletOptions {
	
	private String indentSpaces = "4";
	
	private TldVersion version = TldVersion.VERSION_21;
	
	private boolean formatOutput = true;
	
	private License license = License.NONE;
	
	private boolean doNotOverwrite = false;

        private String inheritTLD = "";

	public DocletOptions withIndentSpaces(String indentSpaces) {
		this.indentSpaces = indentSpaces;
		return this;
	}
	
	public DocletOptions withVersion(TldVersion version) {
		this.version = version;
		return this;
	}
	
	public DocletOptions withFormatOutput(boolean formatOutput) {
		this.formatOutput = formatOutput;
		return this;
	}
	
	public DocletOptions withLicense(License license) {
		this.license = license;
		return this;
	}
	
	public DocletOptions withOverwrite(boolean overwrite) {
		this.doNotOverwrite = overwrite;
		return this;
	}

        public DocletOptions withInherit(String path) {
                this.inheritTLD = path;
                return this;
        }

	public String getIndentSpaces() {
		return indentSpaces;
	}

	public TldVersion getVersion() {
		return version;
	}

	public boolean isFormatOutput() {
		return formatOutput;
	}

	public License getLicense() {
		return license;
	}

	public boolean doNotOverwrite() {
		return doNotOverwrite;
	}
	
        public String getInheritTLD() {
                return inheritTLD;
        }
}
