/**
 * 
 */
package baseClasses;
import javax.faces.application.FacesMessage;
/**
 * @author kaikun
 *
 */
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
 

 
@FacesConverter("productConverter")
public class ProductConverter implements Converter {
	

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                ProductList service = (ProductList) fc.getExternalContext().getApplicationMap().get("productList");
                for (int i=0;i<service.size();i++) if (value.compareTo(Integer.toString(service.getProducts().get(i).getId())) == 0) return  service.getProducts().get(i);
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
       return null;
        
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Product) object).getId());
        }
        else {
            return null;
        }
    }

} 
