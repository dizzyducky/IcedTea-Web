/* DownloadOptions.java
   Copyright (C) 2011 Red Hat, Inc

This file is part of IcedTea.

IcedTea is free software; you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, version 2.

IcedTea is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
IcedTea; see the file COPYING. If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is making a
combined work based on this library. Thus, the terms and conditions of the GNU
General Public License cover the whole combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent modules, and
to copy and distribute the resulting executable under terms of your choice,
provided that you also meet, for each linked independent module, the terms and
conditions of the license of that module. An independent module is a module
which is not derived from or based on this library. If you modify this library,
you may extend this exception to your version of the library, but you are not
obligated to do so. If you do not wish to do so, delete this exception
statement from your version. */

package net.sourceforge.jnlp;

public class DownloadOptions {

    public static final DownloadOptions NONE = new DownloadOptions(false, false);

    private final boolean usePack200;
    private final boolean useVersion;

    public DownloadOptions(boolean usePack, boolean useVersion) {
        this.usePack200 = usePack;
        this.useVersion = useVersion;
    }

    public boolean useExplicitPack() {
        return usePack200;
    }

    public boolean useExplicitVersion() {
        return useVersion;
    }

    @Override
    public String toString() {
        return "DownloadOptions[use pack: " + usePack200 + "; use version: " +
            useVersion + "]";
    }

}
