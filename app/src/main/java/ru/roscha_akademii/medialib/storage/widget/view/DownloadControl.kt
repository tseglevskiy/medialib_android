package ru.roscha_akademii.medialib.storage.widget.view

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.roscha_akademii.medialib.common.MediaLibApplication
import ru.roscha_akademii.medialib.storage.Storage
import ru.roscha_akademii.medialib.storage.model.StorageStatus
import ru.roscha_akademii.medialib.storage.stub.StorageStub
import ru.roscha_akademii.medialib.storage.widget.DownloadControlInterface
import ru.roscha_akademii.medialib.storage.widget.presenter.DownloadControlPresenterImpl
import javax.inject.Inject


class DownloadControl @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0)
    : View(context,
        attrs,
        defStyleAttr),
        DownloadControlView,
        DownloadControlInterface {

    @Inject
    lateinit var storage: Storage

    private val mainHandler: Handler by lazy {
        Handler()
    }

    override var url: String? = null
        set(value) {
            field = value

            mainHandler.post {
                presenter.url = value
            }
        }

    override var title: String? = null
        set(value) {
            field = value

            mainHandler.post {
                presenter.title = value
            }
        }

    @InjectPresenter
    lateinit var presenter: DownloadControlPresenterImpl


    @ProvidePresenter
    fun createPresenter(): DownloadControlPresenterImpl {
        return DownloadControlPresenterImpl(storage)
    }

    override var parentDelegate: MvpDelegate<*>? = null
        set(value) {
            field = value

            val mvpDelegate = MvpDelegate<DownloadControl>(this)
            mvpDelegate.setParentDelegate(value, hashCode().toString())

            mvpDelegate.onCreate()
            mvpDelegate.onAttach()
        }

    private var yesPaint = Paint()
    private var noPaint = Paint()
    private var textPaint = Paint()

    private var strokeWidth: Float

    private var rect = RectF()

    private var status = StorageStatus.REMOTE
    private var percent: Int? = null
    private var percentStr = "0%"
    private var angle = 0f

    init {
        val app = context.applicationContext
        when (app) {
            is MediaLibApplication -> app
                    .component
                    .inject(this)
            else -> {
                storage = StorageStub()
            }
        }

        val dm = resources.displayMetrics

        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, dm)

        yesPaint.style = Paint.Style.STROKE
        yesPaint.color = Color.GREEN
        yesPaint.strokeWidth = strokeWidth

        noPaint.style = Paint.Style.STROKE
        noPaint.color = Color.GRAY
        noPaint.strokeWidth = strokeWidth

        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.color = noPaint.color
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.stop()
    }

    override fun showStatus(status: StorageStatus, percent: Int?) {
        this.status = status

        this.percent = percent
        percentStr = "$percent%"
        angle = (percent?.toFloat() ?: 0f) * 360f / 100f

        invalidate()

        when (status) {
            StorageStatus.REMOTE -> {
                setOnClickListener {
                    presenter.saveLocal()
                }
            }

            StorageStatus.PROGRESS, StorageStatus.LOCAL -> {
                setOnClickListener {
                    presenter.removeLocal()
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (status) {
            StorageStatus.REMOTE -> {
                textPaint.color = noPaint.color

                val xPos = canvas.width / 2
                val yPos = (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2).toInt()
                canvas.drawText("--", xPos.toFloat(), yPos.toFloat(), textPaint)

                canvas.drawOval(rect, noPaint)
            }

            StorageStatus.PROGRESS -> {
                textPaint.color = noPaint.color

                val xPos = canvas.width / 2
                val yPos = (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2).toInt()
                canvas.drawText(percentStr, xPos.toFloat(), yPos.toFloat(), textPaint)

                canvas.drawOval(rect, noPaint)
                canvas.drawArc(rect, -90f, angle, false, yesPaint)

            }

            StorageStatus.LOCAL -> {
                textPaint.color = yesPaint.color

                val xPos = canvas.width / 2
                val yPos = (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2).toInt()
                canvas.drawText("100%", xPos.toFloat(), yPos.toFloat(), textPaint)

                canvas.drawOval(rect, yesPaint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        val size = if (width > height) height else width
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rect.left = 0f + paddingLeft + strokeWidth / 2
        rect.top = 0f + paddingTop + strokeWidth / 2
        rect.bottom = h.toFloat() - paddingBottom - strokeWidth / 2
        rect.right = w.toFloat() - paddingRight - strokeWidth / 2

        val maxH = (rect.bottom - rect.top) * 0.5f
        val maxW = (rect.right - rect.left) * 0.85f
        val maxD = (rect.right - rect.left) * 0.9f

//        val text = "99%"
        val text = "100%"

        val bounds = Rect()

        var min = 1f
        var max = 300f
        while (max - min > 0.8) {
            val mid = (max + min) / 2

            textPaint.textSize = mid
            textPaint.getTextBounds(text, 0, text.length, bounds)

            val text_check_h = bounds.height().toDouble()
            val text_check_w = bounds.width().toDouble()
            val text_diagonal = Math.sqrt(text_check_h * text_check_h + text_check_w * text_check_w)

            if (text_check_h < maxH && text_check_w < maxW && text_diagonal < maxD) {
                min = mid
            } else {
                max = mid
            }
        }

    }
}

