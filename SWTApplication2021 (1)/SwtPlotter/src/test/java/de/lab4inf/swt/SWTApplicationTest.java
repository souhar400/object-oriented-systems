/*
 * Project: SwtPlotter
 *
 * Copyright (c) 2008-2020,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for computer sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.lab4inf.swt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.swt.SWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Basic SWTApplication test.
 *
 * @author nwulff
 * @since  27.03.2020
 * @version $Id: $
 * @param <T> type of the generic class
 */
public class SWTApplicationTest {
    SWTApplication app;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        app = new SWTApplication();
    }

    /**
     * Very basic test to see if the SWT.jar is installed...
     */
    @Test
    public void testSWTInstalled() {
        assertEquals(0, SWT.NONE);
        assertEquals(256, SWT.HORIZONTAL);
    }

    /**
     * Test method for {@link de.lab4inf.swt.SWTApplication#getDisplay()}.
     */
    @Test
    public void testGetDisplay() {
        if (null == app)
            app = new SWTApplication();
        assertNotNull(app.getDisplay());
    }

    /**
     * Test method for {@link de.lab4inf.swt.SWTApplication#getShell()}.
     */
    @Test
    public void testGetShell() {
        if (null == app)
            app = new SWTApplication();
        assertNotNull(app.getShell());
    }

}
