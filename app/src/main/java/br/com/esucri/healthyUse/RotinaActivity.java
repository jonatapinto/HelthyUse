package br.com.esucri.healthyUse;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.sql.Date;
import br.com.esucri.healthyUse.controller.RotinaController;
import br.com.esucri.healthyUse.model.Rotina;
import br.com.esucri.healthyUse.utils.Parsers;
import br.com.esucri.healthyUse.utils.Validations;

@TargetApi(Build.VERSION_CODES.O)
@RequiresApi(api = Build.VERSION_CODES.O)
public class RotinaActivity extends AppCompatActivity {

    EditText editNome, editTipo, editHoraInicio, editHoraFinal, editDataFinal;
    CheckBox checkBoxDomingo, checkBoxSegunda, checkBoxTerca, checkBoxQuarta, checkBoxQuinta, checkBoxSexta,
             checkBoxSabado, checkBoxWhatsApp, checkBoxInstagram, checkBoxFacebook;
    Button botaoGravarRotina;
    TextView textViewAplicativos, textViewDiasSemana;
    Rotina editarRotina = new Rotina();
    Parsers parsers = new Parsers();
    String idRotina;
    Rotina rotina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotina);

        //Intent intent = getIntent();
        //editarRotina = (Rotina) intent.getSerializableExtra("rotina_escolhida");

        //Buscar valores da activity por ID
        editNome = (EditText) findViewById(R.id.editNome);
        editTipo = (EditText) findViewById(R.id.editTipo);
        editHoraInicio = (EditText) findViewById(R.id.editHoraInicio);
        editHoraFinal = (EditText) findViewById(R.id.editHoraFinal);
        editDataFinal = (EditText) findViewById(R.id.editDataFinal);
        checkBoxDomingo = (CheckBox) findViewById(R.id.checkBoxDomingo);
        checkBoxSegunda = (CheckBox) findViewById(R.id.checkBoxSegunda);
        checkBoxTerca = (CheckBox) findViewById(R.id.checkBoxTerca);
        checkBoxQuarta = (CheckBox) findViewById(R.id.checkBoxQuarta);
        checkBoxQuinta = (CheckBox) findViewById(R.id.checkBoxQuinta);
        checkBoxSexta = (CheckBox) findViewById(R.id.checkBoxSexta);
        checkBoxSabado = (CheckBox) findViewById(R.id.checkBoxSabado);
        checkBoxWhatsApp = (CheckBox) findViewById(R.id.checkBoxWhatsApp);
        checkBoxInstagram = (CheckBox) findViewById(R.id.checkBoxInstagram);
        checkBoxFacebook = (CheckBox) findViewById(R.id.checkBoxFacebook);
        botaoGravarRotina = (Button) findViewById(R.id.botaoGravarRotina);
        textViewAplicativos = (TextView) findViewById(R.id.textViewAplicativos);
        textViewDiasSemana = (TextView) findViewById(R.id.textViewDiasSemana);

        //Criando mascara para campos
        SimpleMaskFormatter smfData = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwData = new MaskTextWatcher(editDataFinal, smfData);
        editDataFinal.addTextChangedListener(mtwData);

        SimpleMaskFormatter smfHora = new SimpleMaskFormatter("NN:NN");
        MaskTextWatcher mtwHoraInicio = new MaskTextWatcher(editHoraInicio, smfHora);
        editHoraInicio.addTextChangedListener(mtwHoraInicio);
        MaskTextWatcher mtwHoraFinal = new MaskTextWatcher(editHoraFinal, smfHora);
        editHoraFinal.addTextChangedListener(mtwHoraFinal);

        //if(editarRotina !=null){
        //    botaoGravarRotina.setText("Modificar");
        //} else{
        //    botaoGravarRotina.setText("Gravar");
        //}

        //Se existir valor ao id em Intent, preenche os campos da tela tal como o objeto passado
        idRotina = this.getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(idRotina)) {
            RotinaController crud = new RotinaController(getBaseContext());
            rotina = crud.getById(Integer.parseInt(idRotina));

            editNome.setText(rotina.getNome());
            editTipo.setText(rotina.getTipo());
            editHoraInicio.setText(rotina.getHoraInicio().toString());
            editHoraFinal.setText(rotina.getHoraFinal().toString());
            editDataFinal.setText(rotina.getDataFinal().toString().isEmpty()?"":rotina.getDataFinal().toString());
            checkBoxDomingo.setChecked(rotina.getDom() == 1?true:false);
            checkBoxSegunda.setChecked(rotina.getSeg() == 1?true:false);
            checkBoxTerca.setChecked(rotina.getTer() == 1?true:false);
            checkBoxQuarta.setChecked(rotina.getQua() == 1?true:false);
            checkBoxQuinta.setChecked(rotina.getQui() == 1?true:false);
            checkBoxSexta.setChecked(rotina.getSex() == 1?true:false);
            checkBoxSabado.setChecked(rotina.getSab() == 1?true:false);
            checkBoxWhatsApp.setChecked(rotina.getInstagram() == 1?true:false);
            checkBoxInstagram.setChecked(rotina.getFacebook() == 1?true:false);
            checkBoxFacebook.setChecked(rotina.getWhatsapp() == 1?true:false);
        }

        botaoGravarRotina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar(v);
            }
        });
    }

    public void salvar(View view) {
        if (!validaCampos()) {
            return;
        }
        Rotina rotina = new Rotina();
        rotina.setNome(editNome.getText().toString());
        rotina.setTipo(editTipo.getText().toString());
        rotina.setHoraInicio(parsers.parserStringToTime(editHoraInicio.getText().toString()));
        rotina.setHoraFinal(parsers.parserStringToTime(editHoraFinal.getText().toString()));
        rotina.setDom(checkBoxDomingo.isChecked()?1:0);
        rotina.setSeg(checkBoxSegunda.isChecked()?1:0);
        rotina.setTer(checkBoxTerca.isChecked()?1:0);
        rotina.setQua(checkBoxQuarta.isChecked()?1:0);
        rotina.setQui(checkBoxQuinta.isChecked()?1:0);
        rotina.setSex(checkBoxSexta.isChecked()?1:0);
        rotina.setSab(checkBoxSabado.isChecked()?1:0);
        rotina.setWhatsapp(checkBoxWhatsApp.isChecked()?1:0);
        rotina.setFacebook(checkBoxFacebook.isChecked()?1:0);
        rotina.setInstagram(checkBoxInstagram.isChecked()?1:0);
        //Tratamento por não ser campo obrigatório
        if (editDataFinal.getText().toString().isEmpty()) {
            Date date = null;
            rotina.setDataFinal(date);
        } else{
            rotina.setDataFinal(parsers.parserStringToDate(editDataFinal.getText().toString()));
        }

        RotinaController crud = new RotinaController(getBaseContext());

        long retorno;
        if (TextUtils.isEmpty(idRotina)) {
            retorno = crud.create(rotina);
        } else {
            rotina.setId(Integer.parseInt(idRotina));
            retorno = crud.update(rotina);
        }

        if (retorno == -1) {
            Toast.makeText(RotinaActivity.this, "Erro ao salvar o registro!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(RotinaActivity.this, "Registro salvo com sucesso!", Toast.LENGTH_LONG).show();
            limpar();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validaCampos() {
        Boolean result = true;
        String textoDescricao;
        Boolean marcacaocheckBox;
        Validations validacao = new Validations();

        textoDescricao = editNome.getText().toString().trim();
        if (TextUtils.isEmpty(textoDescricao)) {
            editNome.requestFocus();
            editNome.setError("Campo obrigatório!");
            return false;
        }

        textoDescricao = editHoraInicio.getText().toString().trim();
        if (TextUtils.isEmpty(textoDescricao)) {
            editHoraInicio.requestFocus();
            editHoraInicio.setError("Campo obrigatório!");
            return false;
        } else {
            if(!validacao.isTimeValid(editHoraInicio.getText().toString())){
                //Toast.makeText(RotinaActivity.this, "Favor informar um horario válido!", Toast.LENGTH_LONG).show();
                editHoraInicio.requestFocus();
                editHoraInicio.setError("Hora Inicio inválida!");
                return false;
            }
        }

        textoDescricao = editHoraFinal.getText().toString().trim();
        if (TextUtils.isEmpty(textoDescricao)) {
            editHoraFinal.requestFocus();
            editHoraFinal.setError("Campo obrigatório!");
            return false;
        } else {
            if(!validacao.isTimeValid(editHoraFinal.getText().toString())){
                //Toast.makeText(RotinaActivity.this, "Favor informar um horario válido!", Toast.LENGTH_LONG).show();
                editHoraFinal.requestFocus();
                editHoraFinal.setError("Hora Final inválida!");
                return false;
            }
        }

        //Verifica se algum dos aplicativos foi selecionados
        if (!checkBoxFacebook.isChecked() &&
                !checkBoxWhatsApp.isChecked() &&
                    !checkBoxInstagram.isChecked()){
            Toast.makeText(RotinaActivity.this, "Favor selecionar algum aplicativo!", Toast.LENGTH_LONG).show();
            textViewAplicativos.setError("Opção obrigatória!");
            return false;
        }

        //Verifica se algum dia foi selecionado
        if(!checkBoxDomingo.isChecked() &&
            !checkBoxSegunda.isChecked() &&
                !checkBoxTerca.isChecked() &&
                    !checkBoxQuarta.isChecked() &&
                        !checkBoxQuinta.isChecked() &&
                            !checkBoxSexta.isChecked() &&
                                !checkBoxSabado.isChecked() ){
            Toast.makeText(RotinaActivity.this, "Favor selecionar algum dia da semana!", Toast.LENGTH_LONG).show();
            textViewDiasSemana.setError("Opção obrigatória!");
            return false;
        }

        //if (!validacao.isDateValid(editDataFinal.getText().toString())){
        //    Toast.makeText(RotinaActivity.this, "Favor informar uma data válida!", Toast.LENGTH_LONG).show();
        //    editDataFinal.setError("Data inválida!");
        //    return false;
        //}

        return result;
    }

    public void excluir(View view) {

        if (TextUtils.isEmpty(idRotina)) {
            Toast.makeText(getBaseContext(),
                    "Este produto ainda não está no banco de dados!", Toast.LENGTH_LONG).show();
            return;
        }

        RotinaController crud = new RotinaController(getBaseContext());
        long resultado = crud.delete(rotina);

        limpar();

        if (resultado == -1) {
            Toast.makeText(getBaseContext(),"Erro ao excluir produto!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(),"Produto excluído com sucesso!", Toast.LENGTH_LONG).show();
        }
    }

    private void limpar() {
        editNome.setText("");
        editTipo.setText("");
        editHoraInicio.setText("");
        editHoraFinal.setText("");
        editDataFinal.setText("");
        checkBoxDomingo.setChecked(false);
        checkBoxSegunda.setChecked(false);
        checkBoxTerca.setChecked(false);
        checkBoxQuarta.setChecked(false);
        checkBoxQuinta.setChecked(false);
        checkBoxSexta.setChecked(false);
        checkBoxSabado.setChecked(false);
        checkBoxWhatsApp.setChecked(false);
        checkBoxInstagram.setChecked(false);
        checkBoxFacebook.setChecked(false);
    }
}
