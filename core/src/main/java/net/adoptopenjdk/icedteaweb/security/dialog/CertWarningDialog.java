package net.adoptopenjdk.icedteaweb.security.dialog;

import net.adoptopenjdk.icedteaweb.client.util.gridbag.GridBagPanelBuilder;
import net.adoptopenjdk.icedteaweb.i18n.Translator;
import net.adoptopenjdk.icedteaweb.jnlp.element.information.InformationDesc;
import net.adoptopenjdk.icedteaweb.logging.Logger;
import net.adoptopenjdk.icedteaweb.logging.LoggerFactory;
import net.adoptopenjdk.icedteaweb.security.dialog.result.AccessWarningResult;
import net.sourceforge.jnlp.JNLPFile;
import net.sourceforge.jnlp.security.CertVerifier;
import net.sourceforge.jnlp.security.SecurityUtil;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Optional;

import static net.adoptopenjdk.icedteaweb.i18n.Translator.R;
import static net.adoptopenjdk.icedteaweb.ui.swing.SwingUtils.htmlWrap;

/**
 * TODO: advancedOptions button
 * TODO: CertificateUtils.saveCertificate logic after runButton is pressed when alwaysTrustSelected
 * TODO: bottomPanel of old CertWarningPane
 * <p>
 * Required input
 * - Current certificate path
 * - is root of current path in CA trust store
 * - list of issues with current certificate path
 */
abstract class CertWarningDialog extends BasicSecurityDialog<AccessWarningResult> {
    private static final Logger LOG = LoggerFactory.getLogger(CertWarningDialog.class);
    private static final Translator TRANSLATOR = Translator.getInstance();

    private final CertVerifier certVerifier;
    private final JNLPFile file;
    private boolean initiallyAlwaysTrustedSelected;
    private final Certificate certificate;

    protected CertWarningDialog(final String message, final JNLPFile file, final CertVerifier certVerifier, boolean initiallyAlwaysTrustedSelected) {
        super(message);
        this.file = file;
        this.certVerifier = certVerifier;
        this.certificate = certVerifier.getPublisher(null);
        this.initiallyAlwaysTrustedSelected = initiallyAlwaysTrustedSelected;
    }

    @Override
    public String createTitle() {
        // TODO localization
        return initiallyAlwaysTrustedSelected ? "Security Approval Required" : "Security Warning";
    }

    @Override
    protected JComponent createDetailPaneContent() {
        final GridBagPanelBuilder gridBuilder = new GridBagPanelBuilder();
        try {
            final String name = Optional.ofNullable(file)
                    .map(JNLPFile::getInformation)
                    .map(InformationDesc::getTitle)
                    .orElse(TRANSLATOR.translate("SNoAssociatedCertificate"));
            gridBuilder.addKeyValueRow(TRANSLATOR.translate("Name"), name);

            String publisher = "";
            if (certificate instanceof X509Certificate) {
                publisher = SecurityUtil.getCN(((X509Certificate) certificate)
                        .getSubjectX500Principal().getName());
            }
            gridBuilder.addKeyValueRow(TRANSLATOR.translate("Publisher"), publisher);

            final String from = Optional.ofNullable(file)
                    .map(JNLPFile::getInformation)
                    .map(InformationDesc::getHomepage)
                    .map(URL::toString)
                    .orElse(TRANSLATOR.translate("SNoAssociatedCertificate"));
            gridBuilder.addKeyValueRow(TRANSLATOR.translate("From"), from);

            gridBuilder.addHorizontalSpacer();

            gridBuilder.addComponentRow(createAlwaysTrustCheckbox());

            gridBuilder.addComponentRow(createMoreInformationPanel());

        } catch (final Exception e) {
            LOG.error("Error while trying to read properties for CertWarningDialog!", e);
        }
        return gridBuilder.createGrid();
    }

    protected JCheckBox createAlwaysTrustCheckbox() {
        JCheckBox alwaysTrustCheckBox = new JCheckBox(R("SAlwaysTrustPublisher"));
        alwaysTrustCheckBox.setEnabled(true);
        alwaysTrustCheckBox.setSelected(initiallyAlwaysTrustedSelected);
        return alwaysTrustCheckBox;
    }

    protected JPanel createMoreInformationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        final String moreInformationText = getMoreInformationText();
        final JLabel moreInformationLabel = new JLabel(htmlWrap(moreInformationText));
        panel.add(moreInformationLabel);
        JButton moreInfoButton = new JButton(TRANSLATOR.translate("ButMoreInformation"));
        // TODO use Dialogs here?
        moreInfoButton.addActionListener((e) -> new NewDialogFactory().showMoreInfoDialog(certVerifier, file));
        panel.add(moreInfoButton);
        panel.setPreferredSize(new Dimension(600, 100));
        return panel;
    }

    protected abstract String getMoreInformationText();
}
