import java.util.List;
import java.util.ArrayList;

public class TrainController {
    // نأخذ نسخة واحدة من كلاس الـ Backend الإداري الكبير
    private TrainPath backendGraph;

    // مشد الكلاس يقوم بتهيئة الغراف
    public TrainController() {
        this.backendGraph = new TrainPath();
    }

    // 1. وسيط لإضافة محطة جديدة من الواجهات
    public boolean addStationFromUI(String name, String code) {
        // نتحقق من أن المدخلات ليست فارغة قبل إرسالها للـ Backend
        if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty()) {
            return false; 
        }
        backendGraph.addStation(name, code);
        return true;
    }

    // 2. وسيط لإضافة مسار بين محطتين
    public boolean addPathFromUI(String sourceName, String destName, double distance) {
        if (distance <= 0) return false; // أمان البيانات لمنع المسافات السالبة
        
        Station source = backendGraph.findStationByName(sourceName);
        Station dest = backendGraph.findStationByName(destName);
        
        if (source != null && dest != null) {
            backendGraph.addPath(sourceName, destName, distance);
            return true;
        }
        return false; // أحد المحطات غير موجود
    }

    // 3. تحويل مخرجات أقصر طريق (Dijkstra) إلى نص منسق لعرضه في نافذة (JOptionPane)
    public String getShortestPathRoute(String from, String to) {
    if (from == null || to == null || from.trim().isEmpty() || to.trim().isEmpty()) {
        return "الرجاء تحديد محطة البداية والنهاية بشكل صحيح!";
    }
    
    // استدعاء الدالة المعدلة من الـ Backend بنجاح
    List<Station> path = backendGraph.findShortestPath(from, to);
    
    if (path == null || path.isEmpty()) {
        return "لا يوجد مسار يربط بين المحطتين المكتوبتين أو أن إحداهما غير موجودة.";
    }
    
    // بناء النص المنسق بشكل احترافي لعرضه بالواجهات
    StringBuilder result = new StringBuilder("المسار الأقصر المكتشف هو:\n\n");
    for (int i = 0; i < path.size(); i++) {
        result.append(path.get(i).getName());
        if (i < path.size() - 1) {
            result.append(" ➔ ");
        }
    }
    return result.toString();
}

    // 4. وسيط لفحص الدورات المغلقة وإعطاء نتيجة نصية واضحة للواجهة
    public String checkNetworkCycles() {
        // استدعاء دالة كشف الدورات DFS من الـ Backend
        boolean hasCycle = backendGraph.hasCycle(); 
        
        if (hasCycle) {
            return "⚠️ تنبيه: تم كشف وجود دورة مغلقة في شبكة القطارات الحالية!";
        } else {
            return "✅ الشبكة سليمة تماماً ولا تحتوي على أي دورات مغلقة.";
        }
    }

    // 5. جلب أسماء المحطات فقط لتغذية القوائم المنسدلة (JComboBox) في الـ UI
    public String[] getStationNamesForComboBox() {
        // نأخذ المحطات الحقيقية من الغراف
        List<Station> stations = new ArrayList<>(backendGraph.getNetwork().keySet());
        String[] names = new String[stations.size()];
        
        for (int i = 0; i < stations.size(); i++) {
            names[i] = stations.get(i).getName(); // نأخذ الاسم النصي فقط
        }
        return names;
    }
    
    // 6. وسيط لاستيراد الملف النصي وإعادة تقرير للواجهة
    public String importNetworkFromFile(String filePath) {
        try {
            backendGraph.importNetwork(filePath);
            return "✅ تم استيراد شبكة القطارات بنجاح من الملف!";
        } catch (Exception e) {
            return "❌ فشل الاستيراد: خطأ في قراءة أو تحليل بيانات الملف. " + e.getMessage();
        }
    }
}